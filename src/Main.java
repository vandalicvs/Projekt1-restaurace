import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import com.pavel.restaurace.*;
import com.pavel.restaurace.Menu;


public class Main {
    public static void main(String[] args) {
        System.out.println();
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu();
        DishList dishList = new DishList();
        Cook cook = new Cook(menu,dishList);
        Orders orders = new Orders();
        Management management = new Management(orders);

        int width = 100;
        int height = 30;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, 14));

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString("RESTAURACE", 10, 20);


        for (int y = 0; y < height; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {

                sb.append(image.getRGB(x, y) == -16777216 ? " " : "*");

            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            System.out.println(sb);
        }

        String input = "";
        System.out.println();
        System.out.println();
        System.out.println("© 1988 IBM Corp. ");
        System.out.println();

        while (!input.equals("x")) {
            System.out.println("(m) management interface");
            System.out.println("(o) orders interface");
            System.out.println("(c) cook interface");
            System.out.println("For exit press 'x'");

            input = scanner.nextLine();

            if (input.equals("m")) {
                management.managementRun();
            } else if (input.equals("c")) {
                cook.cookRun();
            } else if (input.equals("o")) {
                orders.ordersRun(menu);
            } else if (!input.equals("x")) {
                System.out.println("Invalid input, please try again.");
            }
        }

        System.out.println("Exiting program.");
        scanner.close();
    }
}