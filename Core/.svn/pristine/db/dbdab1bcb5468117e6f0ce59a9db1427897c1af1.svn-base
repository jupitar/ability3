package com.ustcinfo.mobile.platform.core.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@SuppressLint("NewApi")
public class MyAccessibilityService extends AccessibilityService {

	public static int INVOKE_TYPE = 2;
	public static final int TYPE_KILL_APP = 1;
	public static final int TYPE_INSTALL_APP = 2;
	public static final int TYPE_UNINSTALL_APP = 3;

	public static void reset() {
		INVOKE_TYPE = 0;
	}

	String[] PACKAGES = { "com.android.packageinstaller" };

	@Override
	protected void onServiceConnected() {
//		 init();
	}

	/**
	 * 这样还是获取不到事件
	 */
	private void init() {
		Log.i("test", "config success!");
		// android:accessibilityEventTypes="typeAllMask"
		// android:accessibilityFeedbackType="feedbackGeneric"
		// android:accessibilityFlags="flagDefault"
		// android:canRetrieveWindowContent="true"
		// android:description="@string/accessibility_service_description"
		// android:notificationTimeout="100
		AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
		// accessibilityServiceInfo.packageNames = PACKAGES;
		accessibilityServiceInfo.flags = AccessibilityServiceInfo.DEFAULT;
		accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
		accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
		accessibilityServiceInfo.notificationTimeout = 1000;
		try {
			Method method = AccessibilityServiceInfo.class
					.getDeclaredMethod("setCapabilities");
			method.invoke(accessibilityServiceInfo, 0x00000001);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("test", accessibilityServiceInfo.toString());
		setServiceInfo(accessibilityServiceInfo);
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		this.processAccessibilityEnvent(event);
	}

	private void processAccessibilityEnvent(AccessibilityEvent event) {

		Log.d("test", event.eventTypeToString(event.getEventType()));
		if (event.getSource() == null) {
			Log.d("test", "the source = null");
		} else {
			Log.d("test", "event = " + event.toString());
			switch (INVOKE_TYPE) {
			case TYPE_KILL_APP:
				processKillApplication(event);
				break;
			case TYPE_INSTALL_APP:
				processinstallApplication(event);
				break;
			case TYPE_UNINSTALL_APP:
				processUninstallApplication(event);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected boolean onKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return true;

	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

	private void processUninstallApplication(AccessibilityEvent event) {

		if (event.getSource() != null) {
			if (event.getPackageName().equals("com.android.packageinstaller")) {
				List<AccessibilityNodeInfo> ok_nodes = event.getSource()
						.findAccessibilityNodeInfosByText("确定");
				if (ok_nodes != null && !ok_nodes.isEmpty()) {
					AccessibilityNodeInfo node;
					for (int i = 0; i < ok_nodes.size(); i++) {
						node = ok_nodes.get(i);
						if (node.getClassName().equals("android.widget.Button")
								&& node.isEnabled()) {
							node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						}
					}

				}
			}
		}

	}

	private void processinstallApplication(AccessibilityEvent event) {

		if (event.getSource() != null) {
			checkTitle(event.getSource(), "test");
			if (event.getPackageName().equals("com.android.packageinstaller")) {
				List<AccessibilityNodeInfo> unintall_nodes = event.getSource()
						.findAccessibilityNodeInfosByText("安装");
				if (unintall_nodes != null && !unintall_nodes.isEmpty()) {
					AccessibilityNodeInfo node;
					for (int i = 0; i < unintall_nodes.size(); i++) {
						node = unintall_nodes.get(i);
						if (node.getClassName().equals("android.widget.Button")
								&& node.isEnabled()) {
							node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						}
					}
				}

				List<AccessibilityNodeInfo> next_nodes = event.getSource()
						.findAccessibilityNodeInfosByText("下一步");
				if (next_nodes != null && !next_nodes.isEmpty()) {
					AccessibilityNodeInfo node;
					for (int i = 0; i < next_nodes.size(); i++) {
						node = next_nodes.get(i);
						if (node.getClassName().equals("android.widget.Button")
								&& node.isEnabled()) {
							node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						}
					}
				}

				List<AccessibilityNodeInfo> ok_nodes = event.getSource()
						.findAccessibilityNodeInfosByText("打开");
				if (ok_nodes != null && !ok_nodes.isEmpty()) {
					AccessibilityNodeInfo node;
					for (int i = 0; i < ok_nodes.size(); i++) {
						node = ok_nodes.get(i);
						if (node.getClassName().equals("android.widget.Button")
								&& node.isEnabled()) {
							node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
						}
					}
				}

			}
		}

	}

	/**
	 * 检查是否是要安装应用的标题
	 * 
	 * @param source
	 * @param title
	 * @return
	 */
	private boolean checkTitle(AccessibilityNodeInfo source, String title) {
		List<AccessibilityNodeInfo> titleList = source
				.findAccessibilityNodeInfosByText(title);
		for (AccessibilityNodeInfo node : titleList) {
			Log.d("test", node.toString());
			if (node.getClassName().equals("android.widget.TextView")) {
				return true;
			}
		}
		return false;
	}

	private void processKillApplication(AccessibilityEvent event) {

		if (event.getSource() != null) {
			if (event.getPackageName().equals("com.android.settings")) {
				List<AccessibilityNodeInfo> stop_nodes = event.getSource()
						.findAccessibilityNodeInfosByText("强行停止");
				if (stop_nodes != null && !stop_nodes.isEmpty()) {
					AccessibilityNodeInfo node;
					for (int i = 0; i < stop_nodes.size(); i++) {
						node = stop_nodes.get(i);
						if (node.getClassName().equals("android.widget.Button")) {
							if (node.isEnabled()) {
								node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							}
						}
					}
				}

				List<AccessibilityNodeInfo> ok_nodes = event.getSource()
						.findAccessibilityNodeInfosByText("确定");
				if (ok_nodes != null && !ok_nodes.isEmpty()) {
					AccessibilityNodeInfo node;
					for (int i = 0; i < ok_nodes.size(); i++) {
						node = ok_nodes.get(i);
						if (node.getClassName().equals("android.widget.Button")) {
							node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							Log.d("action", "click ok");
						}
					}

				}
			}
		}
	}

}
