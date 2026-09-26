/*
import java.util.Scanner;

public class ProductDao {
    Scanner sc = new Scanner(System.in);
    // 商品数组，最多存100个
    Product[] productArray = new Product[100];
    int count = 0; // 当前商品数量

    // 1. 添加商品
    public void addProduct() {
        if(count >= productArray.length){
            System.out.println("商品数量已满！");
            return;
        }
        System.out.println("请输入商品ID：");
        String pid = sc.next();
        sc.nextLine(); //吃掉ID后面的换行
        
        System.out.println("请输入商品名称：");
        String pname = sc.nextLine();
        
        System.out.println("请输入商品价格：");
        // 读取价格数字
        double pprice = sc.nextDouble();
        sc.nextLine(); // 【新增】把价格输入后的换行符吃掉！
        
        productArray[count] = new Product(pid, pname, pprice);
        count++;
        System.out.println("商品添加成功");
    }
    

    // 2. 根据ID查找商品
    public void findById(){
        System.out.println("请输入要查找的商品ID");
        String pid = sc.next();
        boolean found = false;
        for(int i = 0; i < count; i++){
            if(productArray[i].id.equals(pid)){
                Product p = productArray[i];
                System.out.println("ID\t名称\t价格");
                System.out.printf("%s\t%s\t%.2f%n", p.id, p.name, p.price);
                found = true;
                break;
            }
        }
        if(!found){
            System.out.println("未找到该商品");
        }
    }


    // 3.修改商品价格
    public void updatePrice(){
        System.out.println("请输入要修改的商品ID：");
        String pid = sc.next();
        sc.nextLine(); // 吃掉ID后面的换行
        boolean found = false;
        for(int i = 0;i<count;i++)
        {
            if(productArray[i].id.equals(pid))
            {
                System.out.println("请输入需要修改的价格");
                double newPrice = sc.nextDouble();
                sc.nextLine(); // 吃掉价格输入后的换行
                productArray[i].price = newPrice;
                found = true;
                break;
            }
        }
    
        if(!found){
            System.out.println("未找到该商品，修改失败");
        }else{
            System.out.println("价格修改成功！");
        }
    }

    // 4. 根据ID删除商品
    public void deleteProduct(){
        System.out.println("输入需要删除的产品ID");
        String pid = sc.next();
        sc.nextLine(); // 吃掉ID后面的换行
        boolean found = false;
        int delIndex = -1;
    
        // 第一步：先查找要删除的下标
        for(int i =0;i<count;i++)
        {
            if(productArray[i].id.equals(pid))
            {
                delIndex = i;
                found = true;
                break; // 找到就退出循环
            }
        }
    
        // 如果找到了，执行移位
        if(found){
            for(int j = delIndex;j < count -1;j++)
            {
                productArray[j] = productArray[j+1];
            }
            count--; // 商品数量-1
            System.out.println("已删除该产品");
        }else{
            System.out.println("未找到该商品，删除失败");
        }
    }
    

    // 5. 展示所有商品
    /*
    public void showAll(){
        if(count==0){
            System.out.println("输出暂无商品");
        }else{
            System.out.println("产品ID      产品名      产品价格");
            for(int i=0;i<count;i++)
            {
                System.out.println(productArray[i].id+" "+productArray[i].name+" "+productArray[i].price);
            }
        }
    }
    public void showAll(){
        if(count==0){
            System.out.println("暂无商品");
        }else{
            System.out.println("产品ID\t产品名\t产品价格");
            for(int i=0;i<count;i++)
            {
                Product p = productArray[i];
                System.out.printf("%s\t%s\t%.2f%n", p.id, p.name, p.price);
            }
        }
    }
    


}
*/
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;



public class ProductDao {
    Scanner sc = new Scanner(System.in);

    // ========== JDBC版本 添加商品 ==========
    public void addProduct(){
        // 1. 控制台输入商品信息
        System.out.println("请输入商品ID：");
        String pid = sc.next();
        sc.nextLine(); // 吃掉换行
        System.out.println("请输入商品名称：");
        String pname = sc.nextLine();
        System.out.println("请输入商品价格：");
        double pprice = sc.nextDouble();
        sc.nextLine();
        // 2. 写预编译SQL
        String sql = "INSERT INTO product(id,name,price) VALUES(?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            //3. 获取数据库连接
            conn = DBUtil.getConnection();
            //4. 将sql交给数据库预编译
            pstmt = conn.prepareStatement(sql);
            //5. 给三个问号依次赋值，下标从1开始
            pstmt.setString(1, pid);    // 第1个? 商品id
            pstmt.setString(2, pname);  // 第2个? 商品名
            pstmt.setDouble(3, pprice); // 第3个? 价格
    
            // ======【补上这一行！执行SQL】======
            int rows = pstmt.executeUpdate();
    
            // 判断受影响行数，给出成功提示
            if(rows > 0){
                System.out.println("✅ 商品添加成功！");
            }else{
                System.out.println("⚠️ 添加失败，没有插入任何数据");
            }
    
        } catch (SQLException e) {
            // 捕获数据库异常
            e.printStackTrace();
            System.out.println("❌ 添加商品异常！可能ID重复（主键不能重复）");
        } finally {
            
            DBUtil.close(conn,pstmt);
        }
    }
    

        // 2. 根据ID查找商品
        public void findById(){
            System.out.println("请输入要查找的商品ID");
            String pid = sc.next();
            boolean found = false;
            // 2. 正确查询SQL
            String sql = "select * from product where id = ?";
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null; // 查询必须加结果集对象！
            try {
                //3. 获取数据库连接
                conn = DBUtil.getConnection();
                //4. 将sql交给数据库预编译
                pstmt = conn.prepareStatement(sql);
                //5. 给问号赋值，下标从1开始
                pstmt.setString(1, pid);
                //6. 查询用 executeQuery，返回ResultSet
                rs = pstmt.executeQuery();
        
                // rs.next() 移动游标，有数据返回true
                if(rs.next()){
                    found = true;
                    // 取出数据库里面的字段
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    System.out.println("✅ 查询到商品：");
                    System.out.printf("ID:%s  商品名:%s  价格:%.2f\n",id,name,price);
                }else{
                    System.out.println("❌ 未找到该ID商品");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("❌ 查找商品异常！");
            } finally {
                // 关闭资源，ResultSet也要关闭！
                DBUtil.close(conn,pstmt,rs);
            }
        }        
        /**
 * 根据商品ID 修改商品价格
 */
    public void updatePrice(){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要修改的商品ID：");
        String pid = sc.next();
        sc.nextLine(); //吸收换行

        System.out.println("请输入新的商品价格：");
        double newPrice = sc.nextDouble();
        sc.nextLine();

        String sql = "update product set price = ? where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 获取数据库连接
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            // 重点：顺序和sql问号一一对应！
            pstmt.setDouble(1, newPrice);
            pstmt.setString(2, pid);      

            // executeUpdate 返回受影响行数
            int rows = pstmt.executeUpdate();
            if(rows > 0){
                System.out.println("✅ 商品价格修改成功！");
            }else{
                System.out.println("❌ 未找到该ID商品，修改失败");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ 修改商品发生数据库异常！");
        } finally {
            // 关闭资源，没有ResultSet就不传
            try{
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        sc.close();
    }
    /**
 * 根据商品ID 删除商品记录
 */
    public void deleteProduct(){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要删除的商品ID：");
        String pid = sc.next();
        sc.nextLine(); //吸收换行符
        String sql = "delete from product where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // 获取数据库连接
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            // 重点：顺序和sql问号一一对应！
            pstmt.setString(1, pid);
            // executeUpdate 返回受影响行数
            int rows = pstmt.executeUpdate();
            if(rows > 0){
                System.out.println("✅ 删除商品成功！");
            }else{
                System.out.println("❌ 未找到该ID商品，删除失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ 删除数据库异常！");
        } finally {
            // 关闭资源，没有ResultSet就不传
            try{
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    /**
 * 查询全部商品，展示所有商品列表 JDBC版
 */
public void showAll(){
    System.out.println("=====商品列表=====");
    String sql = "select id,name,price from product";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try{
        //1. 获取数据库连接
        conn = DBUtil.getConnection();
        //2. 预编译SQL
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        System.out.println("商品ID\t商品名称\t商品价格");
        //4. 遍历rs，取出每一行数据
        boolean hasData = false;
        while(rs.next()){
            hasData = true;
            // 取出id、name、price
            String id = rs.getString("id");
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            // 打印一行商品信息
            System.out.println(id + "\t" + name + "\t" + price);
        }
        //5. 判断是否没有任何商品
        if(!hasData){
            System.out.println("暂无商品数据！");
        }
    }catch(Exception e){
        e.printStackTrace();
        System.out.println("查询全部商品异常！");
    }finally {
        DBUtil.close(conn,pstmt,rs);
    }
}







}
