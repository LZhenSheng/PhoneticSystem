package com.example.phoneticsystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.base.BaseTitleActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UploadFileListener;

/***
 * 用户信息相关
 * @author 胜利镇
 * @time 2020/8/15 15:24
 */
public class UserMessageActivity extends BaseTitleActivity {

    private static final String TAG = "ProfileActivity";

    /***
     * 省
     */
    String provinceName;

    /***
     * 市
     */
    String cityName;

    /***
     * 地区
     */
    String areaName;

    /***
     * 性别
     */
    String sex;
    /***
     * 图片路径
     */
    String picPath;
    /**
     * 头像
     */
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    /**
     * 昵称
     */
    @BindView(R.id.et_nickname)
    EditText et_nickname;

    /**
     * 性别
     */
    @BindView(R.id.tv_gender)
    TextView tv_gender;

    /**
     * 地区
     */
    @BindView(R.id.tv_area)
    TextView tv_area;

    /**
     * 个人介绍
     */
    @BindView(R.id.et_description)
    EditText et_description;

    /**
     * 手机号
     */
    @BindView(R.id.tv_phone)
    TextView tv_phone;

    /***
     * 为各控件注册点击事件
     * @param view
     */
    @OnClick({R.id.avatar_container, R.id.gender_container,R.id.area_container,R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.avatar_container:
                selectImage();
                break;
            case R.id.gender_container:
                GenderDialogFragment
                        .show(getSupportFragmentManager(),0,((dialog, which) -> {
                            //关闭对话框
                            dialog.dismiss();
                            switch (which){
                                case 1:
                                    sex="男";
                                    tv_gender.setText(sex);
                                    break;
                                case 2:
                                    sex="女";
                                    tv_gender.setText(sex);
                                    break;
                            }
                        }));
                break;
            case R.id.area_container:

                //城市选择器初始化
                RegionSelector.init(this).start(this);

                break;
            case R.id.save:
                if(picPath!=null){
                    BmobFile bmobFile = new BmobFile(new File(picPath));
                    bmobFile.uploadblock(new UploadFileListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                ToastUtil.successShortToast("上传文件成功:");
                                PreferenceUtil.saveUserUri(bmobFile.getFileUrl());
                            }else{
                                ToastUtil.errorShortToast("上传文件失败：" );
                                d(e.getMessage());
                            }
                        }

                        @Override
                        public void onProgress(Integer value) {
                        }
                    });
                }
                PreferenceUtil.saveUserSex(sex);
                BmobQuery<Account> bmobQuery=new BmobQuery<>();
                bmobQuery.findObjects(new FindListener<Account>() {
                    @Override
                    public void done(List<Account> list, BmobException e) {
                        if(e==null){
                            for(int i=0;i<list.size();i++){
                                if(list.get(i).getNiceName().equals(et_nickname.getText().toString())){
                                    PreferenceUtil.saveUserNickName(et_nickname.getText().toString());
                                }
                            }
                        }
                    }
                });
                PreferenceUtil.saveUserNickName(et_nickname.getText().toString());
                PreferenceUtil.saveProvince(provinceName);
                PreferenceUtil.saveCity(cityName);
                PreferenceUtil.saveArea(areaName);
                PreferenceUtil.saveDescription(et_description.getText().toString());
                BmobUtil.updateMessage();
                EventBus.getDefault().post(new OnUserChangedEvent());
                break;
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        //头像
        if(PreferenceUtil.getUserUri()!=null){
            ImageUtil.showAvatar(getMainActivity(), iv_avatar,PreferenceUtil.getUserUri() );
        }

        //昵称
        if(PreferenceUtil.getUserNickName()!=null){
            et_nickname.setText(PreferenceUtil.getUserNickName());
        }

        if(PreferenceUtil.getUserSex()!=null){
            tv_gender.setText(PreferenceUtil.getUserSex());
        }

        if(PreferenceUtil.getProvince()!=null){
            tv_area.setText(getResources().getString(R.string.area_value2, PreferenceUtil.getProvince(),PreferenceUtil.getCity(),PreferenceUtil.getArea()));
        }
        //描述
        if (StringUtils.isNotBlank(PreferenceUtil.getDescription())) {
            et_description.setText(PreferenceUtil.getDescription());
        }

        //手机号
        tv_phone.setText(PreferenceUtil.getUserAccount());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
    }


    @Override
    public void initData() {
        super.initData();
        lightStatusBar(R.color.main_color);
    }

    /**
     * 选择图片
     */
    private void selectImage() {
        //进入相册
        //以下是例子
        //用不到的api可以不写
        PictureSelector
                .create(getMainActivity())
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                //.compressSavePath(getPath())//压缩图片保存地址
                //.freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
                //.circleDimmedLayer()// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                //.selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(50)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(50)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                //.videoQuality()// 视频录制质量 0 or 1 int
                //.videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //.videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //请求成功了

            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    //选择了媒体回调

                    //获取选择的资源
                    List<LocalMedia> datum = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    picPath = datum.get(0).getPath();

                    Glide.with(getMainActivity()).load(picPath).into(iv_avatar);
                    break;
                case RegionSelector.REQUEST_REGION:
                    //城市选择
                    //这里的Id和iOS那边城市选择框架的Id不一样
                    //这里我们没有用到所以没多大影响
                    //真实项目中要保持一致

                    //省
                    Region province = RegionSelector.getProvince(data);

                    //市
                    Region city = RegionSelector.getCity(data);

                    //区
                    Region area = RegionSelector.getArea(data);

                    //设置数据
                    //省
                    provinceName=province.getName();

                    //市
                    cityName=city.getName();

                    //区
                    areaName=area.getName();

                    //显示地区
                    tv_area.setText(getResources().getString(R.string.area_value2,
                            provinceName,
                            cityName,
                            areaName));
                    break;
            }
        }
    }

}

