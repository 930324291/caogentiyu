package com.football.net.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.football.net.R;


public class MyRatingBar extends LinearLayout {
	
	ImageView[] imageView = new ImageView[5];

	public MyRatingBar(Context context) {
		super(context);
	}

	public MyRatingBar(Context context, AttributeSet attri) {
		super(context,attri);
		 LayoutInflater.from(context).inflate(R.layout.ratingbar_layout, this, true);
		 imageView[0]  = (ImageView) findViewById(R.id.imag1);
		 imageView[1]  = (ImageView) findViewById(R.id.imag2);
		 imageView[2]  = (ImageView) findViewById(R.id.imag3);
		 imageView[3]  = (ImageView) findViewById(R.id.imag4);
		 imageView[4]  = (ImageView) findViewById(R.id.imag5);
//		imageView[0] = (ImageView) getChildAt(0);
//		imageView[1] = (ImageView) getChildAt(1);
//		imageView[2] = (ImageView) getChildAt(2);
//		imageView[3] = (ImageView) getChildAt(3);
//		imageView[4] = (ImageView) getChildAt(4);
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
	}
	
	public void setRating(float rating){
		rating = rating <0 ?0:rating;
		rating = rating >5 ?5:rating;
		int showNum = (int)rating;
		float showFloat = rating - showNum;
		setNormShowDras(showNum);
		if(showNum < 5 && showNum > 0 && showFloat > 0){
			setFloatDras(showNum,showFloat);
		}
		
	}
	
	private void setNormShowDras(int rating){
		for (int i = 0; i < rating; i++) {
			imageView[i].setImageResource(R.mipmap.ratingbar_show);
		}
	}
	
	private void setFloatDras(int index,float rating){
		
		Bitmap normalRes = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ratingbar_normal);
		Bitmap picRes = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ratingbar_show);
		int width = picRes.getWidth();
		int height = picRes.getHeight();
		int ratingWidth = (int) (width*rating);
		Bitmap showBit = Bitmap.createBitmap( picRes, 0, 0,ratingWidth, height);
		Bitmap norBit = Bitmap.createBitmap(normalRes , ratingWidth, 0,(width-ratingWidth),height );

		Bitmap newbmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas cv = new Canvas( newbmp );
		cv.drawBitmap(showBit, 0, 0, null);
		cv.drawBitmap(norBit, ratingWidth, 0, null);
		cv.save(Canvas.ALL_SAVE_FLAG);//保存
		cv.restore();//存储  


		imageView[index].setImageBitmap(newbmp);
//		imageView[index-1];
	}

	int starNum;
	public void setClickEnable(){
		for (int i = 0; i < 5; i++) {
			imageView[i].setOnClickListener(listener);
		}
	}

	private OnClickListener listener = new  OnClickListener() {
		@Override
		public void onClick(View view) {
				switch (view.getId()){
					case R.id.imag1:{
						starNum = 1;
						setNormShowDras(1);
						setNotShowIntDras(1);
						break;
					}
					case R.id.imag2:{
						starNum = 2;
						setNormShowDras(2);
						setNotShowIntDras(2);
						break;
					}
					case R.id.imag3:{
						starNum = 3;
						setNormShowDras(3);
						setNotShowIntDras(3);
						break;
					}
					case R.id.imag4:{
						starNum = 4;
						setNormShowDras(4);
						setNotShowIntDras(4);
						break;
					}
					case R.id.imag5:{
						starNum = 5;
						setNormShowDras(5);
						setNotShowIntDras(5);
						break;
					}
				}
		}
	};
	private void setNotShowIntDras(int num){
		for (int i= num; i< 5 ; i++){
			imageView[i].setImageResource(R.mipmap.ratingbar_normal);
		}
	}

	public int getStarNum() {
		return starNum;
	}

}
