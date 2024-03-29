import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo productRepo = new ProductRepo();
        IdService idService = new IdService();
        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

        Product[] products = new Product[]{
                new Product("a", "Apfel"),
                new Product("b", "Birne"),
                new Product("c", "Chili"),
                new Product("d", "Dattel"),
                new Product("e", "Erdnuss"),
                new Product("1", "Aprilia"),
                new Product("2", "Bimota"),
                new Product("3", "Cagiva"),
                new Product("4", "Ducati"),
                new Product("5", "Energica")
        };
        productRepo.removeProduct("1");
        for (Product product:products) {
            productRepo.addProduct(product);
        }
        CommandLineInterpreter cli = new CommandLineInterpreter(shopService);
        String fileName = "transactions.txt";
        try {
            cli.executeFile(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("could not find file '" + fileName + "'");
        }
    }
}
