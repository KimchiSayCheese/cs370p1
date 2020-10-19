import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;

public class App {
    private boolean clearCache = false;
    private File inputFile;
    private File outputFile;
    private FileWriter fileWriter;
    public static HashMap<UUID, Item> db;
    public static HashMap<String, UUID> keyRel;

    private BufferedWriter writer;
    private Scanner sc;
    
    public App(String inputFile, String outputFile, String cacheState) {
        if(Boolean.valueOf(cacheState)) {
            clearCache = true;
        }
        
        this.inputFile = new File(new File(".").getAbsolutePath() + "/txt/" + inputFile);
        this.outputFile = new File(new File(".").getAbsolutePath() + "/txt/" + outputFile);
        
        db = new HashMap<UUID, Item>();
        keyRel = new HashMap<String, UUID>();


        try {
            sc = new Scanner(this.inputFile);
            // in an event of using an outfile that has query logs, clear to avoid conflicts
            writer = new BufferedWriter(new FileWriter(this.outputFile));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // initializes the database(hashmap) and then fills the output file with information from thh
    // input file. passing boolean argument in args(clearCache) will determine whether query logs 
    // that manipulates the items be transferred over to the output. more on readme
    public void init() throws Exception {
       
        
        while(sc.hasNextLine()) {
            // split the line of text from its delimiter | and put the strings into a string array.
            // set a swtich block on index 1 which is the CRUD operator and depending on the operation, 
            // set up the database accordingly.
            String line = sc.nextLine();
            String strArr[] = line.split("\\|");
            // trimming empty space
            for(int i = 0; i < strArr.length; i++) {
                strArr[i] = strArr[i].trim();
            }
            
            switch(strArr[1].toLowerCase()) {
                // creates an object containing information about the shopping product that was added into the db and then added into
                // the hash table using UUID as the key and the custom object as its value.
                case "add":
               // System.out.println(Arrays.toString(strArr));
                    Date parseDate = Main.sdf.parse(strArr[0]);
                    Timestamp timestamp = new Timestamp(parseDate.getTime());
                    Item temp = new Item(strArr[2], strArr[3], strArr[4], strArr[5], strArr[6], timestamp, strArr[7]);
                    db.put(UUID.fromString(strArr[7]), temp);
                    break;
                case "delete":
                    db.remove(UUID.fromString(strArr[2]));
                    break;
                case "modify":
                    //System.out.println(Arrays.toString(strArr));
                    db.get(UUID.fromString(strArr[2])).modify(strArr[3], strArr[4]);;
                    break;
                default:
                    break;
            }

            if(!clearCache) {
                write(line);
            }
        }
        
        
        if(clearCache) {
            for (Map.Entry mapElement : db.entrySet()) {
                Item item = (Item)mapElement.getValue();
                String query = item.toString();
                write(query);
            }
            
        }
        
        
        sc.close();

    }
    // function that writes to the transaction log(output)
    private void write(String line) {
        try {
            fileWriter = new FileWriter(this.outputFile, true);
            //writer = new BufferedWriter(new FileWriter(this.outputFile));
            writer = new BufferedWriter(fileWriter);
            if(!clearCache) {
                
            }
            writer.write(line);
            writer.newLine();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        } 
    }

    // for dev purposes. preints out the database in JSON like format.
    public void dbToString() {
        for (Map.Entry mapElement : db.entrySet()) {
            Item item = (Item)mapElement.getValue();
            System.out.println(item.toStringJSON());

        }
    }
    // after the db is populated by the input file, this section deals with addition CRUD
    // operations by user input. currently only the ADD functionality is implemented.
    // The orther CRUD operations will be used in the same exact manner.
    public void start() {
        AtomicBoolean flag = new AtomicBoolean(true);
        boolean firstRun = true;
        Scanner userInput = new Scanner(System.in);
        while(flag.get()) {
            if(firstRun) {
                System.out.println("app has started. enter 'app --help' for more info");
                firstRun = false;
            }

            String response = userInput.nextLine();
            String query;
            // String queryArr[];
            String resArr[] = response.split(" ");
            if(resArr.length > 1) {
                switch(resArr[1]) {
                    case "--help":
                        System.out.println("app flags:\n    --exit: exits app.\n    add 'category | title | item description | price | images'\n");
                        break;
                    case "--exit":
                        flag.set(false);
                        break;
                    case "add":
                        query = response.substring(7).trim();
                        // System.out.println(query);
                        add(query);
                        break;
                    default:
                        break;
                }
            }
            

        }
        userInput.close();
    }

    private void add(String query) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String pkid = UUID.randomUUID().toString();
        String ui[] = query.split("\\|");
        if(ui.length < 5) {
            System.out.println("add [category | title | item description | price | images] << all fields needed!");
            return;
        }
        for(int i = 0; i < ui.length; ++i) {
            ui[i] = ui[i].trim();
        }
        Item item = new Item(ui[0], ui[1], ui[2], ui[3], ui[4], ts, pkid);
        db.put(UUID.fromString(pkid), item);
     
        String writeLine = String.format("%s | ADD | %s | %s | %s | %s | %s | %s", 
        Main.sdf.format(ts).toString(), ui[0], ui[1], ui[2], ui[3], ui[4], UUID.fromString(pkid));
        write(writeLine);
        // 2020/10/15-08:07:37PM | ADD | video games | Nintendo Switch | This bundle is everything you'll ever need | 200 | http://placecorgi.com/250, http://placecorgi.com/250 | acb21d6b-2832-4953-89db-d1781775393e

    }
}


