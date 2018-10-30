package utils;


import android.content.Context;
import android.text.TextUtils;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import zzx.zzxbaselibrary.R;


public class AssetsUtils {

    public static String geFileFromAssets(Context context, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName)) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static int getImageResourceId(String name) {
        R.mipmap mipmap=new R.mipmap();
        //默认的id
        int resId=0x7f02000b;
        try {
            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
            java.lang.reflect.Field field=R.mipmap.class.getField(name);
            //取值
            resId=(Integer)field.get(mipmap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resId;
    }
}
