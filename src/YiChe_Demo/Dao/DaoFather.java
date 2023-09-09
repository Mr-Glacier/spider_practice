package YiChe_Demo.Dao;

import YiChe_Demo.Utils.ReadFileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;

public class DaoFather {
    //父类dao
    private Connection  conn= null;
    private Statement stmt = null;
    //sql操作 所需参数
    protected String driverName;
    protected String connectionUrl;
    protected String userPass;
    protected String userName;
    protected String primaryKey;
    protected String tableName;
    protected String beanName;
    //仅需要更改配置文件路径,以及使用时候的 数据库选择 表选择
    private String jsonpath = "F:\\Workplace\\spider_practice\\src\\YiChe_Demo\\";
    private String filename = "DBConfig.json";

    public DaoFather(int choseDB,int choseTable){
        ReadFileUtil readFileUtil = new ReadFileUtil();
        String content = readFileUtil.MethodReadFile(this.jsonpath,this.filename);
        System.out.println(content);
        JSONObject DBItems1 = JSON.parseObject(content);
        JSONArray DBArray = DBItems1.getJSONArray("Parameter");
        JSONObject DBChose = DBArray.getJSONObject(choseDB);
        this.driverName = DBChose.getString("DBDriver");
        String DBName = DBChose.getString("DBName");
        this.connectionUrl = DBChose.getString("DBConnectionStr")+"databaseName="+DBName;
        this.userName=DBChose.getString("DBUserName");
        this.userPass=DBChose.getString("DBUserPass");
        JSONArray TableArray=DBChose.getJSONArray("EntityList");
        JSONObject TableChose = TableArray.getJSONObject(choseTable);
        this.beanName = TableChose.getString("EntityName");
        this.primaryKey = TableChose.getString("PrimaryKey");
        this.tableName = TableChose.getString("TableName");
        System.out.println("本次调用Dao 参数情况如下:\n本次数据库名称: "+DBName+"\n"+"本次执行表名: "+this.tableName);
    }

    public void MethodCreateSomeObject(){
        try{
            Class.forName(this.driverName);//注册驱动
            if (null==conn ||conn.isClosed()){
                conn = DriverManager.getConnection(this.connectionUrl, this.userName, this.userPass);//创建链接
            }
            if (null==stmt||stmt.isClosed()){
                stmt= conn.createStatement();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void MethodIUD(String str){
        MethodCreateSomeObject();
        try{
            stmt.executeUpdate(str.replace("\n","")
                    .replace("\t","").replace("\r",""));
            //替换插入语句中的特殊情况 例如换行等
            conn.close();
            stmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void MethodInsert(Object obj){
        try {
            Class c = obj.getClass();
            Method[] methods = c.getDeclaredMethods();
            String columnList = "";
            String valueList = "";
            for (Method method : methods) {
                if (method.getName().equals("get" + this.primaryKey)) {
                    continue;
                }
                if (method.getName().startsWith("get")) {
                    String columnName = method.getName().replace("get", "");
                    columnList += columnName + ",";
                    String value = method.invoke(obj) == null ? "-" : method.invoke(obj).toString();
                    //如果为空则替换为-
                    if (method.getReturnType() == String.class) {
                        valueList += "'" + value + "',";
                    } else {
                        valueList += value + ",";
                    }
                }
            }
            columnList = columnList.substring(0,columnList.length()-1);
            valueList = valueList.substring(0,valueList.length()-1);
            String sql = "INSERT INTO "+this.tableName+"( "+columnList+" )values( "+valueList+" )";
            MethodIUD(sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
