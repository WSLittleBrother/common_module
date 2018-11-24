package com.sunwayworld.ui.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sunwayworld.common.R;
import com.sunwayworld.common.dao.model.AppModel;
import com.sunwayworld.common.utils.FileHelperUtil;
import com.sunwayworld.utils.util.AD;
import com.sunwayworld.utils.util.FileUtils;
import com.sunwayworld.utils.util.ImageUtils;
import com.sunwayworld.utils.util.RxjavaUtils;
import com.sunwayworld.utils.util.ScreenUtils;
import com.sunwayworld.utils.util.T;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by 99351 on 2018/1/18.
 */

public class PhotoPreviewDialog extends AppCompatDialog {
    private Context mContext;
    private ImageView photoView;
    private EditText remarkEditText;
    private EditText nameEditText;
    private TextView photoInfoView;
    private TextView postFixInfoTv;
    private TextView saveBtn;
    private TextView deleteBtn;

    //文件路径
    private String filePath = "";
    //文件名称
    private String fileName;
    //创建时间
    private String createTime;

    //记录当前日期和时间
    private Calendar calendar = null;
    private Date date = null;
    private String remake = "";


    public PhotoPreviewDialog(@NonNull Context context, String filePath) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
        this.filePath = filePath;
    }

    public PhotoPreviewDialog(@NonNull Context context, String filePath, String remake) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
        this.filePath = filePath;
        this.remake = remake;
    }


    public PhotoPreviewDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_camera_photo_preview);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = ScreenUtils.getScreenHeight() * 4 / 5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(false);// 点击Dialog外部消失

        initView();
        //设置图片的高度为屏幕的一半
        initData();
        initViewSize();
        setListener();

    }

    /**
     * 初始化控件和布局
     */
    private void initView() {
        photoView = (ImageView) findViewById(R.id.photoView);
        remarkEditText = (EditText) findViewById(R.id.remarkEditText);
        nameEditText = (EditText) findViewById(R.id.photonameEditText);
        photoInfoView = (TextView) findViewById(R.id.photoInfoView);
        saveBtn = (TextView) findViewById(R.id.saveBtn1);
        deleteBtn = (TextView) findViewById(R.id.deleteBtn);
        postFixInfoTv = (TextView) findViewById(R.id.postFixInfoTv);

    }

    ProgressDialog dialog;

    /**
     * 初始化数据以及显示控件
     */
    private void initData() {
        calendar = Calendar.getInstance();
        date = calendar.getTime();

        fileName = new File(filePath).getName();
        createTime = new SimpleDateFormat("yyyy-MM-dd").format(date);

        //经过验证，拍摄的视频地址在这里也能正常显示出图片
        Glide.with(mContext).load(new File(filePath)).error(R.drawable.v2_logo_light).into(photoView);

        photoInfoView.setTextColor(Color.RED);
        photoInfoView.setText(AppModel.getUser().getFullname() + "\t" + createTime);
        nameEditText.setText(FileHelperUtil.getNoSuffixNameByFilename(fileName));
        postFixInfoTv.setText(FileHelperUtil.getSuffixByFilename(fileName));

        //初始化备注信息
        remarkEditText.setText(remake);
    }

    private void setListener() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击保存按钮去处理图片的命名
                File file = new File(filePath);
                //这里获得附件文件夹里面的所有文件
                File[] files = new File(file.getParent()).listFiles();
                for (int i = 0; i < files.length; i++) {
                    //先判断是否有同名的文件
                    if (files[i].getName().equals(nameEditText.getText().toString() + postFixInfoTv.getText().toString()) && !files[i].getName().equals(fileName)) {
                        Toast.makeText(mContext, "文件名已存在，请重新命名", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //在判断文件名是否为空
                if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                    Toast.makeText(mContext, "文件名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //开始重命名文件
                FileUtils.rename(filePath, nameEditText.getText().toString() + postFixInfoTv.getText().toString());
                filePath = filePath.replace(FileHelperUtil.getNoSuffixNameByFilename(fileName), nameEditText.getText().toString());

                //开始保存附件的逻辑
                saveFile();
                PhotoPreviewDialog.this.dismiss();
            }

        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击取消删除文件
                AD.getADBuilder(mContext, "点击继续文件将不保存！", "继续", "返回", new AD.OnClickEventListener() {
                    @Override
                    public void onClickPositive(DialogInterface dialog, int which) {
                        //删除文件
                        FileUtils.deleteFile(filePath);
                        PhotoPreviewDialog.this.dismiss();
                    }

                    @Override
                    public void onClickNegative(DialogInterface dialog, int which) {
                        super.onClickNegative(dialog, which);
                    }

                }).setTitle("警告").create().show();
            }
        });

    }

    /**
     * 保存附件信息到数据库的逻辑
     */
    private void saveFile() {
        RxjavaUtils.ApiObserver(Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                //如果得到的是图片就将图片压缩一下
                if (filePath.toUpperCase().endsWith("PNG") || filePath.toUpperCase().endsWith("JPG") || filePath.toUpperCase().endsWith("JPEG")) {
                    //保存的时候将图片压缩一下，让图片在0.5兆以下
                    File file = new File(filePath);
                    Bitmap src = ImageUtils.getBitmap(file);
                    //获取图片的bitmap对象，这里的bitmap已经是经过尺寸压缩的
                    src = ImageUtils.compressByGeometric(src, 1280, 720);
                    //这里是进行图片的进一步质量压缩处理，让图片在0.5M一下
                    ImageUtils.compressBitmapToFile(src, file, (long) (512 * 1024));
                }
                emitter.onNext("");
                emitter.onComplete();
            }
        }), new RxjavaUtils.ObserverHelper() {

            @Override
            public void onNext(Object o) {
                T.showShort("已保存,请到附件界面查看");
            }

            @Override
            public void onError(Throwable e) {
                T.showShort("保存附件失败，失败原因：" + e.toString());
                FileUtils.deleteFile(filePath);
            }
        });

    }

    /**
     * 初始化图片显示区域的高度为Dialog高度的一半
     */
    private void initViewSize() {
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        // 弹出框的实际内容高度按照屏幕高度的80%计算
        int imageWidth = (int) (display.getWidth() * 0.5);
        int imageHeight = (int) (display.getHeight() * 0.5);
        photoView.setLayoutParams(new RelativeLayout.LayoutParams(imageWidth, imageHeight));
        findViewById(R.id.photoContainer).setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
    }

    /**
     * 用户点击返回键的提示
     */
    @Override
    public void onBackPressed() {
        AD.getADBuilder(mContext, "确定不保存文件？", "确定", "取消", new AD.OnClickEventListener() {
            @Override
            public void onClickPositive(DialogInterface dialog, int which) {
                //删除文件
                FileUtils.deleteFile(filePath);
                PhotoPreviewDialog.this.dismiss();
            }

            @Override
            public void onClickNegative(DialogInterface dialog, int which) {
                super.onClickNegative(dialog, which);
            }

        }).setTitle("警告").create().show();

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
