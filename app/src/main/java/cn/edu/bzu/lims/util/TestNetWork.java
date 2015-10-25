package cn.edu.bzu.lims.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


/**
 * 网络连接工具类
 * @category 检测网络连接，无网络连接时显示设置网络对话框
 * @author monster
 * @date 2015-07-19
 */
public class TestNetWork {
	
	/**
	 * 检测网络连接
	 * @param con
	 * @return
	 */
	public boolean isNetworkAvailable(Context con) {
		ConnectivityManager cm = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		if (netinfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * 提示设置网络连接对话框
	 * @param context
	 */
	public void showNetDialog(final Context context){
		 AlertDialog.Builder builder = new AlertDialog.Builder(context);  
         builder.setTitle("无网络连接").setMessage("是否对网络进行设置?");  
           
         builder.setPositiveButton("是", new DialogInterface.OnClickListener() {  
             @Override  
             public void onClick(DialogInterface dialog, int which) {  
                 Intent intent = null;  
                   
                 try {  
                     @SuppressWarnings("deprecation")
					String sdkVersion = android.os.Build.VERSION.SDK;  
                     if(Integer.valueOf(sdkVersion) > 10) {  
                         intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);  
                     }else {  
                         intent = new Intent();  
                         ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");  
                         intent.setComponent(comp);  
                         intent.setAction("android.intent.action.VIEW");  
                     }  
                     context.startActivity(intent);  
                 } catch (Exception e) {  
                     e.printStackTrace();  
                 }  
             }  
         }).setNegativeButton("否", new DialogInterface.OnClickListener() {  
             @Override  
             public void onClick(DialogInterface dialog, int which) {
                 Toast.makeText(context,"无网络连接",Toast.LENGTH_SHORT).show();
             }  
         }).show();  
	}
}
//修改于:2015年10月23日
