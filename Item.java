import java.util.Arrays;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Item {
    String itemDesc;
    String category;
    String title;
    BigDecimal price;
    String images[];
    Timestamp timestamp;
    String pkid;
    public Item(String category, String title, String itemDesc , String price, String images, Timestamp timestamp, String pkid) {
        this.category = category.trim();
        this.title = title.trim();
        this.itemDesc = itemDesc.trim();
        this.price = new BigDecimal(price);
        this.timestamp = timestamp;
        this.pkid = pkid;
        if(images != null) {
            this.images = images.split(",");
        } else {
            this.images = null;
        }
    }

    //2020/10/15-08:08:43PM | ADD | tickets | MetroCard | It has a $100 value. Cash only. Price is firm. | 80 | http://placecorgi.com/250,http://placecorgi.com/250 | 982cb065-5dad-4dd8-ae24-9438677fe6fa

    @Override
    public String toString() {
        String time = Main.sdf.format(this.timestamp).toString();
        String imageArrStr = Arrays.toString(this.images).replace("[", "").replace("]", "").trim();
        
        return String.format("%s | ADD | %s | %s | %s | %s | %s | %s", time, category, title, itemDesc, price.toString(), imageArrStr, this.pkid);
    }

    public String toStringJSON() {
        String time = Main.sdf.format(timestamp);


        return String.format("{\n  timestamp: %s,\n  title: %s,\n  price: %s,\n  itemDescription: %s,\n  category: %s,\n  images: %s\n},", 
        time, this.title, this.price.toString(), this.itemDesc, this.category, Arrays.toString(this.images));
    }

    public void modify(String updateItemDesc, String newPrice) {
        if(!updateItemDesc.equals("null")) {
            this.itemDesc = updateItemDesc;
        }
        if(!newPrice.equals("null")) {
            this.price = new BigDecimal(newPrice);
        }
    }
}
