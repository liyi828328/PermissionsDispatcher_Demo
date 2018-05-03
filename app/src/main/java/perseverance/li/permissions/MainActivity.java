package perseverance.li.permissions;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.read_sms).setOnClickListener(this);
        findViewById(R.id.write_calllog).setOnClickListener(this);
        findViewById(R.id.write_external_storage).setOnClickListener(this);
        findViewById(R.id.request_all_permissions).setOnClickListener(this);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void readSms() {
        Toast.makeText(this, "Read sms permission", Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission(Manifest.permission.WRITE_CALL_LOG)
    public void writeCallLog() {
        Toast.makeText(this, "Write calllog permission", Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void writeExternalStorage() {
        Toast.makeText(this, "Write external storage permission", Toast.LENGTH_SHORT).show();
    }

    @NeedsPermission({Manifest.permission.READ_SMS, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void getAllPermissions() {
        Toast.makeText(this, "Get all permissions", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_SMS)
    void showReadSms(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("使用此功能需要 READ_SMS 个权限，下一步将继续请求权限")
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//继续执行请求
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.cancel();//取消执行请求
            }
        }).show();
    }

    @OnShowRationale({Manifest.permission.READ_SMS, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("使用此功能需要 3 个权限，下一步将继续请求权限")
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//继续执行请求
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.cancel();//取消执行请求
            }
        }).show();
    }

    @OnPermissionDenied({Manifest.permission.READ_SMS, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void permissionDenied() {
        Toast.makeText(this, "OnPermissionDenied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_SMS)
    void neverAskAgain() {
        Toast.makeText(this, "OnNeverAskAgain", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_sms:
                MainActivityPermissionsDispatcher.readSmsWithPermissionCheck(this);
                break;
            case R.id.write_calllog:
                MainActivityPermissionsDispatcher.writeCallLogWithPermissionCheck(this);
                break;
            case R.id.write_external_storage:
                MainActivityPermissionsDispatcher.writeExternalStorageWithPermissionCheck(this);
                break;
            case R.id.request_all_permissions:
                MainActivityPermissionsDispatcher.getAllPermissionsWithPermissionCheck(this);
                break;
            default:
                break;
        }
    }
}
