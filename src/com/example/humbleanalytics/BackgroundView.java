package com.example.humbleanalytics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;



public class BackgroundView extends View{
	private Paint paint;
	private Bitmap bm;
	private Matrix matrix;
	private int screenWidth;
	private int screenHeight;
	
	
	
	public BackgroundView(Context context, int screenW, int screenH) {
		super(context);
		// TODO Auto-generated constructor stub
		
		if (screenW > screenH){
			
			//this.bm = BitmapFactory.decodeResource(getResources(), R.drawable.christine_land);
			
		}else{
			
			//this.bm = BitmapFactory.decodeResource(getResources(), R.drawable.christine_port);
			
		}
		this.matrix = new Matrix();
		
		this.screenWidth = screenW;
		this.screenHeight = screenH;
		
		float screenWidthDiffrence = this.screenWidth - bm.getWidth();
		float screenHeightDiffrence = this.screenHeight - bm.getHeight();
		
		
		float scaleX = (screenWidthDiffrence+(float)bm.getWidth())/(float)bm.getWidth();
		this.matrix.preScale((screenWidthDiffrence+bm.getWidth())/bm.getWidth(), (screenWidthDiffrence+bm.getWidth())/bm.getWidth());
		
		
		
		//Log.d("scalex: ", ""+ scaleX);
		
			
		/*	
		
		Log.d("bm width:", ""+ bm.getWidth());
		Log.d("bm height:", ""+ bm.getHeight());
		Log.d("screen width:", ""+ screenW);
		Log.d("screen height:", ""+ screenH);
		*/
		
		init();
		
	}
	
	public void init(){
		
		paint = new Paint();
		
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
       
		
	}
    @Override
    protected void onDraw(Canvas canvas) {
    	
    	
        //canvas.drawText("TEEEST", 100, 100, paint);
        canvas.drawBitmap(bm, matrix, paint); 
    	//canvas.drawRect(100, 100, 200, 200, paint);
    	
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	

    	setMeasuredDimension(this.screenWidth, this.screenHeight);	
    }

}
