package gfx;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Fixed extends DisplayObject {
    private Image image;

    public Fixed(Image image) {
        super(image.getWidth(null), image.getHeight(null));
        this.image = image;
    }

    @Override
    //Ghi đè phương thức draw từ lớp cha
    public void draw(Graphics g, double x, double y, double direction) {
        AffineTransform at = new AffineTransform();
        at.setToTranslation(x - width / 2, y - height / 2);
        /**Thiết lập một phép dịch chuyển sao cho hình ảnh sẽ được vẽ tại vị trí (x, y), nhưng được dịch chuyển
         đi một nửa chiều rộng và chiều cao của nó để đảm bảo hình ảnh được vẽ từ trung tâm của nó.*/
        at.rotate(direction + Math.toRadians(90), width / 2, height / 2);//Thiết lập một phép xoay
        ((Graphics2D) g).drawImage(image, at, null);
    }
}
