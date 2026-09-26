public class ProductMain{
    public static void main(String[] args) {
        ProductDao dao = new ProductDao();
        while(true){
            System.out.println("====商品管理系统====");
            System.out.println("1 添加商品");
            System.out.println("2 根据ID查询商品");
            System.out.println("3 修改商品价格");
            System.out.println("4 删除商品");
            System.out.println("5 展示全部商品");
            System.out.println("0 退出系统");
            System.out.println("请输入选项：");
            int select = dao.sc.nextInt();
            switch (select){
                case 1:
                    dao.addProduct();
                    break;
                case 2:
                    dao.findById();
                    break;
                case 3:
                    dao.updatePrice();
                    break;
                case 4:
                    dao.deleteProduct();
                    break;
                case 5:
                    dao.showAll();
                    break;
                case 0:
                    System.out.println("程序退出");
                    return;
                default:
                    System.out.println("输入错误");
            }
        }
    }
}
