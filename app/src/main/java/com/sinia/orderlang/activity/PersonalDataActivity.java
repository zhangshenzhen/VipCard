package com.sinia.orderlang.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.bean.PersonalCenterBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.UpdatePerInfoRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.CacheUtils;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.DateUtils;
import com.sinia.orderlang.views.CustomDatePickerDialog;
import com.sinia.orderlang.views.CustomDatePickerDialog.onDateListener;


/**
 * 个人资料
 */
public class PersonalDataActivity extends BaseActivity implements
		OnClickListener {
	private RelativeLayout rlNickname, rlGender, rl_head, rl_birthday;
	private TextView tvNickname, tvGender, tv_birthday, tvTelephone;
	private ImageView img_icon;
	private LinearLayout layout_cancle, root;
	private String appKey = "c8bde32ba40559884c5fece9236205b4", imgPath,
			dateTime, imgUrl = "";
	private RelativeLayout layout_choose, layout_photo;
	private File sdcardTempFile;
	private PopupWindow avatorPop;
	private PersonalCenterBean personalCenterBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personaldata, "个人资料");
		Bmob.initialize(this, appKey);
		getDoingView().setVisibility(View.GONE);
		personalCenterBean = (PersonalCenterBean) getIntent()
				.getSerializableExtra("personalCenterBean");
		initView();
		initData();
	}

	public void initView() {
		img_icon = (ImageView) findViewById(R.id.img_icon);
		rlNickname = (RelativeLayout) findViewById(R.id.rl_nickname);
		rlGender = (RelativeLayout) findViewById(R.id.rl_gender);
		tvNickname = (TextView) findViewById(R.id.tv_nickname);
		tv_birthday = (TextView) findViewById(R.id.tv_birthday);
		tvGender = (TextView) findViewById(R.id.tv_gender);
		rl_head = (RelativeLayout) findViewById(R.id.rl_head);
		root = (LinearLayout) findViewById(R.id.root);
		rl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
		tvTelephone = (TextView) findViewById(R.id.tv_telephone);

		rlNickname.setOnClickListener(this);
		rlGender.setOnClickListener(this);
		rl_head.setOnClickListener(this);
		rl_birthday.setOnClickListener(this);
	}

	private void initData() {
		BitmapUtilsHelp.getImage(this, R.drawable.default_head).display(
				img_icon, personalCenterBean.getImageUrl());
		tvTelephone.setText(personalCenterBean.getTelephone());
		tvNickname.setText(personalCenterBean.getNickName());
		switch (personalCenterBean.getSex()) {
		case "1":
			tvGender.setText("男");
			break;
		case "2":
			tvGender.setText("女");
			break;
		case "3":
			tvGender.setText("保密");
			break;
		default:
			tvGender.setText("保密");
			break;
		}
		tv_birthday.setText(personalCenterBean.getBirthday());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_nickname:
			Intent it = new Intent(PersonalDataActivity.this,
					ChangeNicknameActivity.class);
			it.putExtra("name",
					tvNickname.getText().toString().equals("输入昵称") ? ""
							: tvNickname.getText().toString());
			startActivityForResult(it, 1);
			break;
		case R.id.rl_gender:
			Intent it2 = new Intent(PersonalDataActivity.this,
					ChangeGenderActivity.class);
			it2.putExtra("sex", personalCenterBean.getSex());
			startActivityForResult(it2, 2);
			break;
		case R.id.rl_head:
			// 打开相机或者打开相册
			changeImage();
			break;
		case R.id.rl_birthday:
			CustomDatePickerDialog dialog = new CustomDatePickerDialog(this,
					Calendar.getInstance());
			dialog.addDateListener(new onDateListener() {

				@Override
				public void dateFinish(Calendar c) {
					String chooseTime = DateUtils.formateTime(c.getTime()
							.toLocaleString());
					String curTime = DateUtils.getCurrentDateTime();
					Log.i("msg", "chooseTime---" + chooseTime + "curTime----"
							+ curTime);
					if (DateUtils.isChooseTimeSmallThanCur(chooseTime, curTime)) {
						personalCenterBean.setBirthday(DateUtils
								.formateDate(chooseTime));
						tv_birthday.setText(DateUtils.formateDate(chooseTime));
					} else {
						// showToast("生日不能晚于今天哦O(∩_∩)O~,------------卖萌可耻T_T");
						showToast("生日不能晚于今天");
					}
				}
			});
			dialog.show();
			break;
		}

	}

	// -----------------------------------相片裁剪---------------------------------------
	@SuppressWarnings("deprecation")
	private void changeImage() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.view_personimg_dialog, null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_cancle = (LinearLayout) view.findViewById(R.id.layout_cancle);

		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String saveDir = Environment.getExternalStorageDirectory()
					+ "/temple";
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			sdcardTempFile = new File(saveDir, "hot_pic_" + "user_img" + ".jpg");
			sdcardTempFile.delete();
			if (!sdcardTempFile.exists()) {
				try {
					sdcardTempFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(PersonalDataActivity.this, "图片文件创建失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}

			layout_photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Date date1 = new Date(System.currentTimeMillis());
					dateTime = date1.getTime() + "";

					layout_choose.setBackgroundColor(Color.WHITE);
					getAvataFromCamera();
					avatorPop.dismiss();
				}
			});
			layout_choose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					layout_photo.setBackgroundColor(Color.WHITE);
					getAvataFromAlbum();
					avatorPop.dismiss();
				}
			});

			avatorPop = new PopupWindow(view,
					getWindow().getAttributes().width, 600);
			avatorPop.setTouchInterceptor(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						avatorPop.dismiss();
						return true;
					}
					return false;
				}
			});

			layout_cancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					avatorPop.dismiss();
				}
			});
			avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
			avatorPop.setHeight(WindowManager.LayoutParams.FILL_PARENT);
			avatorPop.setTouchable(true);
			avatorPop.setFocusable(true);
			avatorPop.setOutsideTouchable(true);
			avatorPop.setBackgroundDrawable(new BitmapDrawable());
			// 动画效果 从底部弹起
			avatorPop.showAtLocation(root, Gravity.BOTTOM, 0, 0);
		}
	}

	protected void getAvataFromAlbum() {
		Intent intent2 = new Intent(Intent.ACTION_PICK);
		intent2.setType("image/*");
		startActivityForResult(intent2, 102);
	}

	protected void getAvataFromCamera() {
		File f = new File(CacheUtils.getCacheDirectory(this, true, "icon")
				+ dateTime + "avatar.jpg");
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri uri = Uri.fromFile(f);
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, 101);
	}

	private void updateIcon(String avataPath) {
		if (avataPath != null) {
			final BmobFile file = new BmobFile(new File(avataPath));
			file.upload(this, new UploadFileListener() {

				@Override
				public void onSuccess() {
					Log.i("temp",
							"图片上传成功"
									+ file.getFileUrl(PersonalDataActivity.this));
					imgUrl = file.getFileUrl(PersonalDataActivity.this);
					personalCenterBean.setImageUrl(imgUrl);
					showToast("图片上传成功");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					Log.i("temp", "图片上传失败" + arg1);
				}
			});
		}
	}

	public String saveToSdCard(Bitmap bitmap) {
		String files = CacheUtils.getCacheDirectory(this, true, "icon")
				+ dateTime + "_11.jpg";
		File file = new File(files);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 280);
		intent.putExtra("outputY", 280);
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 黑边
		intent.putExtra("scaleUpIfNeeded", true);// 黑边
		intent.putExtra("return-data", true);// 选择返回数据
		startActivityForResult(intent, 103);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				Bundle b1 = data.getExtras();
				String nickname = b1.getString("nickname");
				personalCenterBean.setNickName(nickname);
				tvNickname.setText(nickname);
				break;
			case 2:
				Bundle b2 = data.getExtras();
				int gender = b2.getInt("gender");
				Log.d("lamp", "sex=" + gender);
				if (gender == 1) {
					tvGender.setText("男");
				} else if (gender == 2) {
					tvGender.setText("女");
				} else {
					tvGender.setText("保密");
				}
				personalCenterBean.setSex(gender+"");
				break;
			case 101:
				String files = CacheUtils.getCacheDirectory(this, true, "icon")
						+ dateTime + "avatar.jpg";
				File file = new File(files);
				if (file.exists() && file.length() > 0) {
					Uri uri = Uri.fromFile(file);
					startPhotoZoom(uri);
				}
				break;
			case 102:
				if (data == null) {
					return;
				}
				startPhotoZoom(data.getData());
				break;
			case 103:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bitmap = extras.getParcelable("data");
						imgPath = saveToSdCard(bitmap);
						Log.i("temp", "iconUrl---" + imgPath);
						img_icon.setImageBitmap(bitmap);
						updateIcon(imgPath);
					}
				}
				break;
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	// ---------------------------------------------------------------------------

	private void updatePerInfo(String userId, String imageUrl, String sex,
			String nickName, String birthday) {
		UpdatePerInfoRequest request = new UpdatePerInfoRequest();
		request.setMethod("updatePerInfo");
		request.setUserId(userId);
		request.setImageUrl(imageUrl);
		request.setSex(sex);
		request.setNickName(nickName);
		request.setBirthday(birthday);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.PERSONAL_CENTER,
				request.toString());
	}

	@Override
	protected void backView() {
		// TODO Auto-generated method stub
		updatePerInfo(Constants.userId, personalCenterBean.getImageUrl(),
				personalCenterBean.getSex() + "",
				personalCenterBean.getNickName(),
				personalCenterBean.getBirthday());
		finish();
	}

	@Override
	public void onBackPressed() {
		updatePerInfo(Constants.userId, personalCenterBean.getImageUrl(),
				personalCenterBean.getSex() + "",
				personalCenterBean.getNickName(),
				personalCenterBean.getBirthday());
		finish();
	}

}
