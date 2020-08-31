package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.util.AsyncHttpUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button btn_rgt;//声明注册按钮

    RequestParams registerParams = new RequestParams();//声明参数对象

    EditText rgt_account;//声明账号输入框

    EditText rgt_name;//声明姓名输入框

    EditText rgt_pwd;//声明密码输入框

    EditText rgt_shop;//声明店铺输入框

    EditText rgt_user;

    RadioGroup role;//声明角色单选按钮组

    private static final String TEMP_DIR = "zfcx_config";//声明串号文件夹
    private static final String TEMP_FILE_NAME = "zfcx_file";//声明串号文件名

    //activity声明周期，创建方法
    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.register);
        init();
    }

    //实例化各种空间
    private void init() {
        this.rgt_shop = (EditText)findViewById(R.id.register_shop);
        this.rgt_user = (EditText)findViewById(R.id.register_user);
        this.rgt_pwd = (EditText)findViewById(R.id.register_pwd);
        this.rgt_name = (EditText)findViewById(R.id.register_name);
        this.rgt_account = (EditText)findViewById(R.id.register_account);
        this.btn_rgt = (Button)findViewById(R.id.regiter);
        this.role = (RadioGroup)findViewById(R.id.select_role);
        this.role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup param1RadioGroup, int param1Int) {//设置单选框监听，如果选得是店长则参入参数18，否则19
                RadioButton radioButton = (RadioButton)Register.this.findViewById(param1Int);
                if (radioButton.getText().toString().equals("店长"))
                    Register.this.registerParams.put("fuserRole", "18");
                if (radioButton.getText().toString().equals("员工"))
                    Register.this.registerParams.put("fuserRole", "19");
            }
        });
        this.btn_rgt.setOnClickListener(this);//给注册按钮设置监听
    }

    //注册方法
    private void register(String uuid) {
        Log.i("UUID",uuid);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明请求框架
        /**
         * 传入参数
         */
        this.registerParams.put("fuserName", this.rgt_name.getText().toString());
        this.registerParams.put("fuserNo", this.rgt_account.getText().toString());
        String str2 = (new AsyncHttpUrl()).getRegisterUrl();//获取URL
        this.registerParams.put("fuserNumber", uuid);
        this.registerParams.put("fuserTel", this.rgt_user.getText().toString());
        this.registerParams.put("fuserPassword", this.rgt_pwd.getText().toString());
        this.registerParams.put("fshopNo", this.rgt_shop.getText().toString());
        this.registerParams.put("fuserIsconditions", "2");
        asyncHttpClient.post(str2, this.registerParams, (ResponseHandlerInterface)new JsonHttpResponseHandler() {//发起post请求
            //失败方法
            public void onFailure(int param1Int, Header[] param1ArrayOfHeader, Throwable param1Throwable, JSONObject param1JSONObject) { super.onFailure(param1Int, param1ArrayOfHeader, param1Throwable, param1JSONObject); }
            //成功方法
            public void onSuccess(int param1Int, Header[] param1ArrayOfHeader, JSONObject param1JSONObject) {
                super.onSuccess(param1Int, param1ArrayOfHeader, param1JSONObject);
                String str1 = param1JSONObject.optString("code");//获取返回值中得code参数
                String str2 = param1JSONObject.optString("msg");//获取返回值中得msg参数
                if (str1.equals("200")) {//如果code值为200
                    Toast.makeText((Context)Register.this, str2, Toast.LENGTH_SHORT).show();//弹出提示
                    Intent intent = new Intent((Context)Register.this, Login.class);//设置Intent参数
                    Register.this.startActivity(intent);//开始页面跳转
                    Register.this.finish();//销毁注册页面
                    return;
                }
                Toast.makeText((Context)Register.this, str2, Toast.LENGTH_SHORT).show();//弹出提示
            }
        });
    }
    //Android10检测
    private String android10Check(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return createUUID();
        }else {
            return createUUID();
        }
    }

    //创建/获取本地文件中得uuid
    private String createUUID(){
        String uuid = UUID.randomUUID().toString().replace("-","");
        File externalDownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File zfcxFileDir = new File(externalDownloadsDir,TEMP_DIR);
        System.out.println(zfcxFileDir.getPath());
        if (!zfcxFileDir.exists()){
            if (!zfcxFileDir.mkdir()){
                Toast.makeText(this,"文件夹创建失败: " + zfcxFileDir.getPath(),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this,"文件创建失败" + file.getPath(),Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(this,"文件创建失败" + file.getPath(),Toast.LENGTH_SHORT).show();
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
        return uuid;
    }

    /**
     * 查询手机串号方法
     * @param uuid
     */
    private void getDeviceId(final String uuid){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();//声明框架类
        String str2 = (new AsyncHttpUrl()).getDeviceSelectUrl();//获取url
        RequestParams requestParams = new RequestParams();//声明参数对象
        requestParams.put("fuserNumber", uuid);//放置参数
        asyncHttpClient.post(str2, requestParams, new JsonHttpResponseHandler(){
            //成功方法
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("code").equals("200")){
                    Toast.makeText(Register.this,"此手机已被注册",Toast.LENGTH_SHORT);
                }else {
                    register(android10Check());//调用注册方法
                }

            }
            //失败方法
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    //注册按钮点击方法
    @Override
    public void onClick(View paramView) {
        if (paramView.getId() == R.id.regiter){
            if (!this.rgt_shop.getText().toString().trim().equals(null) && !this.rgt_shop.getText().toString().trim().equals("")) {//盘点商铺是否为空
                if (!this.rgt_user.getText().toString().trim().equals(null) && !this.rgt_user.getText().toString().trim().equals("")) {
                    if (!this.rgt_pwd.getText().toString().trim().equals(null) && !this.rgt_pwd.getText().toString().trim().equals("")) {//判断密码是否为空
                        if (!this.rgt_name.getText().toString().trim().equals(null) && !this.rgt_name.getText().toString().trim().equals("")) {//判断姓名是否为空
                            if (!this.rgt_account.getText().toString().trim().equals(null) && !this.rgt_account.getText().toString().trim().equals("")) {//判断账户是否为空
                                getDeviceId(android10Check());
                                return;
                            }
                            Toast.makeText((Context)this, "登录账户不能为空！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText((Context)this, "真实姓名不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText((Context)this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText((Context)this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText((Context)this, "商铺名不能为空！", Toast.LENGTH_SHORT).show();
        }
        }

}
