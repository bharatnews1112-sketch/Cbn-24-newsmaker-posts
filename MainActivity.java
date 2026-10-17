package com.cbn24.newspostmaker;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.*;import android.graphics.drawable.*;import android.net.Uri;import android.provider.MediaStore;import android.view.*;import android.widget.*;import java.io.*;import java.util.*;

public class MainActivity extends Activity {
    EditText headline, body, reporter, location; ImageView preview; Bitmap selected; final int PICK=7;
    int blue=Color.rgb(21,101,192);
    @Override public void onCreate(Bundle b){super.onCreate(b); build();}
    TextView label(String s){TextView t=new TextView(this);t.setText(s);t.setTextSize(16);t.setTextColor(Color.DKGRAY);t.setPadding(4,14,4,6);return t;}
    EditText input(String hint){EditText e=new EditText(this);e.setHint(hint);e.setTextSize(17);e.setPadding(18,12,18,12);GradientDrawable g=new GradientDrawable();g.setColor(Color.WHITE);g.setStroke(1,0xffdddddd);e.setBackground(g);return e;}
    Button btn(String s){Button b=new Button(this);b.setText(s);b.setTextSize(16);b.setTextColor(Color.WHITE);b.setAllCaps(false);GradientDrawable g=new GradientDrawable();g.setColor(blue);g.setCornerRadius(8);b.setBackground(g);return b;}
    void build(){
        ScrollView scroll=new ScrollView(this); LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(18,12,18,24);root.setBackgroundColor(0xfff4f4f4);scroll.addView(root);
        TextView title=new TextView(this);title.setText("CBN 24 NEWS POST MAKER");title.setTextSize(25);title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);title.setTextColor(blue);title.setGravity(Gravity.CENTER);title.setPadding(0,12,0,12);root.addView(title);
        headline=input("मुख्य शीर्षक / Headline"); body=input("समाचार का पूरा विवरण लिखें..."); body.setGravity(Gravity.TOP); body.setMinLines(7); reporter=input("संवाददाता / Reporter"); location=input("स्थान / Location");
        root.addView(label("मुख्य शीर्षक / Headline"));root.addView(headline,new LinearLayout.LayoutParams(-1,65));
        root.addView(label("समाचार का विवरण / News"));root.addView(body,new LinearLayout.LayoutParams(-1,220));
        preview=new ImageView(this);preview.setImageResource(com.cbn24.newspostmaker.R.drawable.cbn24_logo);preview.setScaleType(ImageView.ScaleType.FIT_CENTER);GradientDrawable pg=new GradientDrawable();pg.setColor(0xffdddddd);preview.setBackground(pg);LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,330);pp.topMargin=12;root.addView(preview,pp);
        Button photo=btn("📷  फोटो चुनें");root.addView(photo,new LinearLayout.LayoutParams(-1,62)); photo.setOnClickListener(v->pick());
        root.addView(label("संवाददाता / Reporter"));root.addView(reporter,new LinearLayout.LayoutParams(-1,65));root.addView(label("स्थान / Location"));root.addView(location,new LinearLayout.LayoutParams(-1,65));
        TextView size=new TextView(this);size.setText("Story Size: 1080 × 1920");size.setTextSize(18);size.setTypeface(Typeface.DEFAULT,Typeface.BOLD);size.setGravity(Gravity.CENTER);size.setPadding(0,18,0,10);root.addView(size);
        Button make=btn("📰  पोस्ट तैयार करें");root.addView(make,new LinearLayout.LayoutParams(-1,65));make.setOnClickListener(v->generate());
        setContentView(scroll);
    }
    void pick(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("image/*");i.addCategory(Intent.CATEGORY_OPENABLE);startActivityForResult(i,PICK);}
    @Override protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==PICK&&c==RESULT_OK&&d!=null){try{selected=MediaStore.Images.Media.getBitmap(getContentResolver(),d.getData());preview.setImageBitmap(selected);}catch(Exception e){Toast.makeText(this,"फोटो नहीं खुल सकी",Toast.LENGTH_SHORT).show();}}}
    Bitmap fit(Bitmap b,int w,int h){
        Bitmap out=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);Canvas c=new Canvas(out);c.drawColor(Color.WHITE);
        float scale=Math.min(w/(float)b.getWidth(),h/(float)b.getHeight());float dw=b.getWidth()*scale,dh=b.getHeight()*scale;
        Paint p=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);c.drawBitmap(b,null,new RectF((w-dw)/2f,(h-dh)/2f,(w+dw)/2f,(h+dh)/2f),p);return out;
    }
    ArrayList<String> wrap(String s,Paint p,float max){
        ArrayList<String> out=new ArrayList<>();
        for(String para:s.split("\\n",-1)){
            String line="";for(String word:para.trim().split("\\s+")){
                if(word.isEmpty())continue;String test=line.length()==0?word:line+" "+word;
                if(p.measureText(test)<=max)line=test;else{if(line.length()>0)out.add(line);line=word;}
            }
            if(line.length()>0)out.add(line);else if(para.trim().isEmpty())out.add("");
        }return out;
    }
    void generate(){
        String h=headline.getText().toString().trim(),txt=body.getText().toString().trim();
        if(h.isEmpty()||txt.isEmpty()||selected==null){Toast.makeText(this,"शीर्षक, समाचार और फोटो तीनों भरें",Toast.LENGTH_LONG).show();return;}
        final int W=1080,H=1920,left=42,right=1038,width=right-left;
        Bitmap out=Bitmap.createBitmap(W,H,Bitmap.Config.ARGB_8888);Canvas c=new Canvas(out);c.drawColor(Color.WHITE);
        Paint ink=paint(Color.BLACK,44,true),blueLine=line(blue,5);
        Bitmap raw=BitmapFactory.decodeResource(getResources(),R.drawable.cbn24_logo);
        // The supplied masthead image also contains the app name at its bottom; crop that strip out.
        int cropH=Math.max(1,(int)(raw.getHeight()*0.82f));Bitmap mast=Bitmap.createBitmap(raw,0,0,raw.getWidth(),cropH);
        Bitmap head=fit(mast,width,210);c.drawBitmap(head,left,18,null);c.drawLine(left,240,right,240,blueLine);
        int y=292;ArrayList<String> hl=wrap(h,ink,width);if(hl.size()>3){ink.setTextSize(38);hl=wrap(h,ink,width);}if(hl.size()>4){Toast.makeText(this,"शीर्षक बहुत लंबा है। कृपया छोटा करें।",Toast.LENGTH_LONG).show();return;}
        for(String line:hl){c.drawText(line,left,y,ink);y+=ink.getTextSize()+12;}y+=14;
        int photoY=Math.max(y+4,390);int photoH=440;
        if(photoY+photoH>900)photoY=420;
        c.drawBitmap(fit(selected,width,photoH),left,photoY,null);c.drawLine(left,photoY+photoH+18,right,photoY+photoH+18,line(blue,4));
        int bodyY=photoY+photoH+62;int bodyBottom=1630;int available=bodyBottom-bodyY;
        Paint pb=paint(Color.rgb(45,45,45),30,false);ArrayList<String> lines=wrap(txt,pb,width);int lineGap=39;
        while(lines.size()*lineGap>available && pb.getTextSize()>19){pb.setTextSize(pb.getTextSize()-1);lineGap=(int)(pb.getTextSize()*1.3f);lines=wrap(txt,pb,width);}
        if(lines.size()*lineGap>available){Toast.makeText(this,"खबर 1080×1920 पोस्ट में फिट नहीं हो रही। कृपया खबर थोड़ी छोटी करें।",Toast.LENGTH_LONG).show();return;}
        y=bodyY;for(String line:lines){if(!line.isEmpty())c.drawText(line,left,y,pb);y+=lineGap;}
        y=Math.max(y+18,Math.min(1690,bodyBottom+20));Paint meta=paint(Color.BLACK,27,true);
        String rr=reporter.getText().toString().trim(),loc=location.getText().toString().trim();
        if(!rr.isEmpty()){String m="संवाददाता: "+rr;for(String l:wrap(m,meta,width)){if(y<1780)c.drawText(l,left,y,meta);y+=34;}}
        if(!loc.isEmpty()){String m="स्थान: "+loc;for(String l:wrap(m,meta,width)){if(y<1780)c.drawText(l,left,y,meta);y+=34;}}
        c.drawLine(left,1810,right,1810,line(blue,4));Paint foot=paint(0xff555555,23,false);
        c.drawText("CBN 24 News Post Maker",left,1850,foot);c.drawText("www.citybreakingnews24.online",left,1888,foot);
        try{
            String name="CBN24_"+System.currentTimeMillis()+".jpg";OutputStream os;Uri savedUri=null;
            if(Build.VERSION.SDK_INT>=29){android.content.ContentValues v=new android.content.ContentValues();v.put(MediaStore.Images.Media.DISPLAY_NAME,name);v.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");v.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/CBN24");savedUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,v);if(savedUri==null)throw new IOException("फाइल नहीं बन सकी");os=getContentResolver().openOutputStream(savedUri);}
            else{File dir=new File(getExternalFilesDir(null),"CBN24");if(!dir.exists())dir.mkdirs();File f=new File(dir,name);os=new FileOutputStream(f);}
            out.compress(Bitmap.CompressFormat.JPEG,95,os);os.close();
            Toast.makeText(this,"पोस्ट Pictures/CBN24 में सेव हो गई",Toast.LENGTH_LONG).show();
            if(savedUri!=null){Intent share=new Intent(Intent.ACTION_SEND);share.setType("image/jpeg");share.putExtra(Intent.EXTRA_STREAM,savedUri);share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);startActivity(Intent.createChooser(share,"पोस्ट शेयर करें"));}
        }catch(Exception e){Toast.makeText(this,"पोस्ट सेव नहीं हुई: "+e.getMessage(),Toast.LENGTH_LONG).show();}
    }
    Paint paint(int color,float size,boolean bold){Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);p.setColor(color);p.setTextSize(size);p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));return p;}
    Paint line(int color,float w){Paint p=paint(color,w,false);return p;}
    void showShare(Bitmap b,String name){try{File f=new File(getCacheDir(),name);FileOutputStream o=new FileOutputStream(f);b.compress(Bitmap.CompressFormat.JPEG,95,o);o.close();Toast.makeText(this,"पोस्ट Pictures/CBN24 में सेव हो गई",Toast.LENGTH_LONG).show();}catch(Exception e){}}
}
