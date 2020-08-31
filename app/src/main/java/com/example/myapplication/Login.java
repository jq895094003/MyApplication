package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.myapplication.Bean.Fuser;
import com.example.myapplication.bottom.AbnormalFragmentTwo;
import com.example.myapplication.util.AsyncHttpUrl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {
    //声明uuid存储文件夹
    private static final String TEMP_DIR = "zfcx_config";
    //声明uuid文件名
    private static final String TEMP_FILE_NAME = "zfcx_file";
    //声明登录按钮
    private Button btn_login;
    //声明密码输入框
    private EditText et_password;
    //声明账号输入框
    private EditText et_username;
    //声明传递得对象
    Fuser fuser;
    //声明进度条框
    private ProgressDialog progressDialog;
    //声明内部存储，app卸载则消失
    SharedPreferences sharedPreferences;

    //activity生命周期，启动方法
    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.login);
        getDirAppVersion();//获取当前版本号
        /**
         * 实例化声明得变量
         */
        this.sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        this.et_username = (EditText) findViewById(R.id.username);
        this.et_password = (EditText) findViewById(R.id.password);
        //查询更新方法
        updateApp(getAppVersionName((Context) this));
        //获得权限赋予值
        int i = checkSelfPermission("Manifest.permission.READ_PHONE_STATE");
        if (i != 0) {//判断是否赋予权限，如果否，则弹出申请框
            ActivityCompat.requestPermissions((Activity) this, new String[]{"android.permission.READ_PHONE_STATE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else {//如果是则查询手机串号
            getDeviceId(android10Check());
        }
        //实例化登录按钮
        this.btn_login = (Button) findViewById(R.id.btn_log);
        //设置登录按钮点击监听事件
        this.btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (Login.this.et_username.getText().toString().trim().equals("") || Login.this.et_username.getText().toString().trim().equals(null)) {//如果账号输入框为空或“”则发出提示
                    Toast.makeText((Context) Login.this, "账户名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Login.this.et_password.getText().toString().trim().equals("") || Login.this.et_password.getText().toString().trim().equals(null)) {//如果密码输入框为空或“”则发出提示
                    Toast.makeText((Context) Login.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //查询登录接口
                asyncHttpPost(et_username.getText().toString(), Login.this.et_password.getText().toString());
            }
        });
    }

    /**
     * 查询手机串号方法
     * @param uuid
     */
    private void getDeviceId(final String uuid){
        progressDialog = ProgressDialog.show((Context) this, "请稍后……", "正在获取数据……", true);//开启进度框
        progressDialog.setCancelable(true);//设置进度条框可以被取消
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明框架类
        String str2 = (new AsyncHttpUrl()).getDeviceSelectUrl();//获取url
        RequestParams requestParams = new RequestParams();//声明参数对象
        requestParams.put("fuserNumber", uuid);//放置参数
        asyncHttpClient.post(str2, requestParams, new JsonHttpResponseHandler(){
            //成功方法
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Login.this.progressDialog.dismiss();//关闭进度条框
                System.out.println(response);
                    if (response.optString("code").equals("200")){//如果返回值code参数为200
                        JSONObject data = response.optJSONObject("data");//获取data参数
                        if (data.optString("fuserIsconditions").equals("1")){
                            sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);//实例化内部存储空间
//                        String[] datas = data.split(",");
//                        for (int i = 0; i < datas.length; i++) {
//                            System.out.println(datas[i]);
//                        }
                            SharedPreferences.Editor editor = sharedPreferences.edit();//声明内部存储空间写入对象
                            editor.putString("AAA", data.optString("fshopNo"));//写入参数
                            editor.putString("BBB", data.optString("fuserShopName"));//写入参数
                            editor.putString("Role",data.optString("fuserRole"));//写入参数
                            editor.commit();//提交写入得参数
                            Login.this.et_username.setText(data.optString("fuserTel"));//将手机号设置到账号框
                            return;
                        }else if (data.optString("fuserIsconditions").equals("2")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle("提示");
                            builder.setMessage("该账号未审核，请联系管理员审核账号！");
                            builder.setCancelable(false);
                            builder.show();
                        }else if (data.optString("fuserIsconditions").equals("0")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle("提示");
                            builder.setMessage("该账号已禁用，请联系管理员审核账号！");
                            builder.setCancelable(false);
                            builder.show();
                        }

                    }
                    if (response.optString("code").equals("400")) {//如果返回值状态码为400
                        JSONObject jsonObject = response.optJSONObject("data");
//                        Toast.makeText(Login.this, response.optString("msg"), Toast.LENGTH_SHORT).show();//页面提示
                        Intent intent = new Intent((Context) Login.this, Register.class);//声明Intent对象，传入页面
                        Login.this.startActivity(intent);//跳转页面
                        Login.this.finish();//将当前页面销毁
                        return;
                    }
            }
            //失败方法
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                String str = errorResponse.toString();
                Toast.makeText( Login.this, str, Toast.LENGTH_SHORT).show();//提示错误日志
            }
        });
    }

//    /**
//     * 查询手机串号方法
//     * @param uuid
//     */
//    private void getDeviceId(final String uuid) {
//        progressDialog = ProgressDialog.show((Context) this, "请稍后……", "正在获取数据……", true);//开启进度框
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明框架类
//        String str2 = (new AsyncHttpUrl()).getDeviceSelectUrl();//获取url
//        RequestParams requestParams = new RequestParams();//声明参数对象
//        requestParams.put("fuserNumber", uuid);//放置参数
//        asyncHttpClient.post(str2, requestParams, (ResponseHandlerInterface) new AsyncHttpResponseHandler() {//发起post请求
//            //成功方法
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                if (statusCode == 200) {//如果访问状态码为200
//                    Gson gson = new Gson();
//                    String str1 = new String(responseBody);//将byte数组转换为字符串
//                    JsonObject jsonObject = (new JsonParser()).parse(str1).getAsJsonObject();//将字符串转换为JSON
//                    String str2 = String.valueOf(jsonObject.get("code"));//获取返回值中code参数
//                    String str3 = str2.substring(1, str2.length() - 1);//截取字符串
//                    String str4 = String.valueOf(jsonObject.get("msg"));//获取返回值中msg参数
//                    str4.substring(1, str4.length() - 1);//截取字符串
//                    if (str3.equals("200")) {//如果返回值状态码为200
//                        Log.i("getDeviceId",str1);
//                        String str5 = String.valueOf(jsonObject.get("data"));//获取返回值中data值
//                        String str6 = str5.substring(1, str5.length() - 1);//截取字符串
//                        String[] arrayOfString = str6.split(",");//将data值分割
//                        SharedPreferences.Editor editor = Login.this.sharedPreferences.edit();//声明Editor对象
//                        editor.putString("AAA", arrayOfString[0]);//写入参数
//                        editor.putString("BBB", arrayOfString[3]);//写入参数
//                        editor.commit();//提交写入得参数
//                        Login.this.fuser = new Fuser();//实例化Fuser对象
//                        /**
//                         * 将参数存入Fuser对象
//                         */
//                        Login.this.fuser.setFshopNo(arrayOfString[0]);
//                        Login.this.fuser.setFuserNo(arrayOfString[1]);
//                        Login.this.fuser.setFuserRole(arrayOfString[2]);
//                        Login.this.fuser.setFuserShopName(arrayOfString[3]);
//                        Login.this.fuser.setFuserTel(arrayOfString[4]);
//                        Login.this.et_username.setText(Login.this.fuser.getFuserTel());//将手机号设置到账号框
//                        Login.this.progressDialog.dismiss();//关闭进度条框
//                        return;
//                    }
//                    if (str3.equals("400")) {//如果返回值状态码为400
//                        Login.this.progressDialog.dismiss();//关闭进度条框
//                        Toast.makeText((Context) Login.this, str4, Toast.LENGTH_SHORT).show();//页面提示
//                        Intent intent = new Intent((Context) Login.this, Register.class);//声明Intent对象，传入页面
//                        Login.this.startActivity(intent);//跳转页面
//                        Login.this.finish();//将当前页面销毁
//                        return;
//                    }
//                } else {//如果请求状态码不为200
//                    Login.this.progressDialog.dismiss();//关闭进度条框
//                    System.out.println(responseBody);//打印返回值
//                }
//            }
//
//            //失败方法
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                Login.this.progressDialog.dismiss();//关闭进度条框
//                error.printStackTrace();
//                String str = new String(error.toString());
//                Toast.makeText((Context) Login.this, str, Toast.LENGTH_SHORT).show();//提示错误日志
//            }
//
//        });
//    }


    /**
     * 下载App方法
     */
    private void downLoadApp() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明请求框架
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "支付查询.apk");//获取文件绝对路径
        asyncHttpClient.get("http://123.58.7.107:8081/apk/支付查询.apk", (ResponseHandlerInterface) new FileAsyncHttpResponseHandler(file) {//发起post请求

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
                Login.this.progressDialog.dismiss();//关闭进度条框
                System.out.println(throwable);
                Toast.makeText((Context) Login.this, "下载失败，请退出客户端重试", Toast.LENGTH_SHORT).show();//弹出提示
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                Login.this.progressDialog.dismiss();//关闭进度条框
                Login.this.installApk(file);//调用安装Apk方法
            }

            @Override
            public void onProgress(long param1Long1, long param1Long2) {
                double d1 = param1Long1;//获取进度参数
                Double.isNaN(d1);
                double d2 = d1 * 1.0D;//转换类型
                double d3 = param1Long2;//获取进度总数
                Double.isNaN(d3);
                int i = (int) (100.0D * d2 / d3);//计算进度
                progressDialog.setProgress(i);//设置进度
                super.onProgress(param1Long1, param1Long2);
            }

        });
    }


    //获取版本号方法
    public static String getAppVersionName(Context paramContext) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("获取app版本号得系统时间:");
        stringBuilder.append(simpleDateFormat.format(date));
        printStream.println(stringBuilder.toString());
        try {
            return (paramContext.getApplicationContext().getPackageManager().getPackageInfo(paramContext.getPackageName(), 0)).versionName;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.e("", nameNotFoundException.getMessage());
            return "";
        }
    }

    //获取本地安装包版本号
    private String getDirAppVersion() {
        PackageManager packageManager = getPackageManager();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        stringBuilder.append("/支付查询.apk");
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(stringBuilder.toString(), PackageManager.GET_ACTIVITIES);
        return (packageInfo == null) ? "null" : packageInfo.versionName;
    }

    //安装apk方法
    private void installApk(final File file) {
        (new AlertDialog.Builder((Context) this)).setTitle("安装apk").setMessage("下载已完成，是否更新?").setPositiveButton("是", new DialogInterface.OnClickListener() {//弹出提示框
            //单机事件
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                Uri uri;
                if (!file.exists())//判断文件是否存在
                    return;
                Intent intent = new Intent("android.intent.action.VIEW");//声明Intent对象
                if (Build.VERSION.SDK_INT >= 26) {//判断当前Android版本
                    uri = FileProvider.getUriForFile((Context) Login.this, "com.smrj.myapplication.fileprovider", file);//设置uri
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//添加标记
                } else {
                    uri = Uri.fromFile(file);//设置uri
                }
                intent.setDataAndType(uri, "application/vnd.android.package-archive");//设置安装页面
                Login.this.startActivity(intent);//跳转页面
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {//如果单机得是否
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {//什么都不做
            }
        }).show();
    }

    //检测是否要更新App
    private void updateApp(String paramString) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明请求框架
        String str = (new AsyncHttpUrl()).getUpLoadUrl();//声明请求路径
        RequestParams requestParams = new RequestParams();//声明参数对象
        requestParams.put("userapkname", "支付查询");//添加参数
        requestParams.put("userapkcoed", paramString);
        System.out.println("走到了版本检测");
        asyncHttpClient.post(str, requestParams, (ResponseHandlerInterface) new JsonHttpResponseHandler() {//发起请求
            //成功方法
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String str1 = response.optString("code");//获取返回值中得code值
                final String serviceVersion = response.optString("data");//获取返回值中得data
                if (str1.equals("200"))//如果code为200
                    return;
                if (str1.equals("400")) {//如果code为400
                    System.out.println(serviceVersion);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("更新提示").setMessage("发现新版本，是否更新?").setPositiveButton("是", new DialogInterface.OnClickListener() {//弹出更新框
                        //点击事件
                        public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                            if (Login.this.getDirAppVersion().equals(serviceVersion)) {//如果本地安装包版本跟服务器版本一致
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "支付查询.apk");//获取本地安装包路径
                                Login.this.installApk(file);//调用安装方法
                                return;
                            }
                            progressDialog = new ProgressDialog((Context) Login.this);//弹出进度框
                            Login.this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            Login.this.progressDialog.setTitle("正在下载");
                            Login.this.progressDialog.setMessage("请稍后...");
                            Login.this.progressDialog.setProgress(0);
                            Login.this.progressDialog.setMax(100);
                            Login.this.progressDialog.show();
                            Login.this.progressDialog.setCancelable(false);

                            Login.this.downLoadApp();//调用下载apk方法
                        }
                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {//如果选择否，什么也不做
                        public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        }
                    });
                    if (!Login.this.isFinishing()){
                        builder.show();
                    }
                }
            }
            //失败方法
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Login.this.progressDialog.dismiss();//关闭进度条框
                System.out.println(throwable);
            }
        });
    }

    //登录方法
    public void asyncHttpPost(String paramString1, String paramString2) {
        this.progressDialog = ProgressDialog.show((Context) this, "请稍等……", "获取数据中……", true);//弹出进度条框
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明请求框架
        String uuid = android10Check();//获取uuid
        asyncHttpClient.setEnableRedirects(true);//进度条框可取消
        String str2 = (new AsyncHttpUrl()).getLoginUrl();//获取URL
        RequestParams requestParams = new RequestParams();//声明参数对象
        requestParams.put("fuserNumber", uuid);//填充参数
        requestParams.put("password", paramString2);
        asyncHttpClient.post(str2, requestParams, (ResponseHandlerInterface) new AsyncHttpResponseHandler() {//发起post请求
            //成功方法
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {//如果状态码为200
                    new Gson();//声明GSON对象
                    String str1 = new String(responseBody);
                    JsonObject jsonObject = (new JsonParser()).parse(str1).getAsJsonObject();//将返回值转为JSON
                    String str2 = String.valueOf(jsonObject.get("code"));//取出code值
                    if (str2.substring(1, str2.length() - 1).equals("200")) {//如果返回值中code参数为200
                        Log.i("login",str1);
                        String str3 = String.valueOf(jsonObject.get("data").getAsJsonObject().get("resultToken"));//取出Token值
                        String str4 = str3.substring(1, str3.length() - 1);//截取字符串
                        SharedPreferences.Editor editor = Login.this.sharedPreferences.edit();//声明Editor对象
                        editor.putString("Token", str4);//将Token传入Editor
                        editor.commit();//提交Editor
                        String resultData = String.valueOf(jsonObject.get("data").getAsJsonObject().get("resoultData"));
                        JsonObject fuserRole = new JsonParser().parse(resultData).getAsJsonObject();
                        String role = String.valueOf(fuserRole.get("fuserRole"));
                        if (role.substring(1,role.length() - 1).equals("18")) {//如果用户角色为18
                            Intent intent = new Intent((Context) Login.this, BottomActivity.class);//声明Intent对象
                            Login.this.startActivity(intent);//跳转页面
                        } else {//如果为其他值
                            Intent intent = new Intent((Context) Login.this, AbnormalFragmentTwo.class);//声明Intent对象
                            intent.putExtra("Fuser", (Serializable) Login.this.fuser);//再Intent中放置要传输得数据
                            Login.this.startActivity(intent);//跳转页面
                        }
                        Login.this.progressDialog.dismiss();//关闭弹出框
                        Login.this.finish();//销毁此页面
                        return;
                    }
                    Login.this.progressDialog.dismiss();//关闭弹出框
                    Toast.makeText((Context) Login.this, String.valueOf(jsonObject.get("msg")), Toast.LENGTH_SHORT).show();//弹出提示
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Login.this.progressDialog.dismiss();//关闭弹出框
                Toast.makeText((Context) Login.this, "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show();//弹出提示
                Log.d("statusCode", String.valueOf(statusCode));//打印返回参数
                Log.d("Header", String.valueOf(headers));
                Log.d("ResponseBody", String.valueOf(error));
            }
        });
    }

    //Android10检测
    private String android10Check(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){//比对使用得Android版本
            return createUUID();//创建UUID；
        }else {
            updateUser();//更新数据库中得手机串号
            return createUUID();//创建UUID
        }
    }

    //创建UUID
    private String createUUID(){
        String android_id = Settings.System.getString(
                getContentResolver(), Settings.Secure.ANDROID_ID);
        String uuid = UUID.randomUUID().toString().replace("-","") + android_id;
        File externalDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File zfcxFileDir = new File(externalDownloadsDir,TEMP_DIR);
        Log.i("UUIDDIR",externalDownloadsDir.getAbsolutePath());
        Toast.makeText(Login.this,externalDownloadsDir.getPath(),Toast.LENGTH_LONG);
        if (!zfcxFileDir.exists()){
            if (!zfcxFileDir.mkdir()){
                Toast.makeText(this,"文件夹创建失败: " + externalDownloadsDir.getPath(),Toast.LENGTH_SHORT).show();
            }
        }
        File file = new File(zfcxFileDir,TEMP_FILE_NAME);
        if (!file.exists()){
            FileWriter fileWriter = null;
            try {
                if (file.createNewFile()){
                    fileWriter = new FileWriter(file,false);
                    fileWriter.write(uuid);
                }else{
                    Toast.makeText(this,"文件创建失败" + externalDownloadsDir.getPath(),Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(this,"文件创建失败" + externalDownloadsDir.getPath(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                if (fileWriter != null){
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                uuid = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileReader != null){
                    try {
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Log.i("CREATEUUID",uuid);
        return uuid;
    }

    //更新数据库串号
    private void updateUser(){
        AsyncHttpClient updateUser = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        String str1 = "";
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission((Context)this, "android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED){
            str1 = telephonyManager.getDeviceId();
        }else {
            ActivityCompat.checkSelfPermission((Context)this, "android.permission.READ_PHONE_STATE");
        }
        String url = new AsyncHttpUrl().getUpdateUserDevice();
        requestParams.put("FUSERYNUMBER",str1);
        requestParams.put("FUSERNUMBER",createUUID());
        updateUser.post(url,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("updateResult" , String.valueOf(response));
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public int checkSelfPermission(String paramString) {
        return (ContextCompat.checkSelfPermission((Context) this, "android.permission.READ_PHONE_STATE"));
    }

    //权限返回值回调
    public void onRequestPermissionsResult(int paramInt, @NonNull String[] paramArrayOfString, @NonNull int[] paramArrayOfint) {
        if (paramInt != 1)
            return;
        if (paramArrayOfint.length > 0 && paramArrayOfint[0] == 0)
            return;
        finish();
    }

}
