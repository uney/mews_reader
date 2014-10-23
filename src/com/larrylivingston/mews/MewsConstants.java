package com.larrylivingston.mews;

import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.larrylivingston.mews.objects.MewsList;
import com.larrylivingston.mews.objects.Mews;
import com.larrylivingston.utilities_module.DbAdapter;

public class MewsConstants {
	private static final String TAG = "MewsConstants";
	/**
	 * SERVER parameters go here
	 */
	public static final String SERVER_PARAM_LAT = "lat";
	public static final String SERVER_PARAM_LNG = "lng";
	public static final String SERVER_PARAM_PAGE_NO = "page";
	public static final String SERVER_PARAM_USERNAME = "user_name";
	public static final String SERVER_PARAM_CATEGORY = "category";
	public static final String SERVER_PARAM_MODE = "mode";
	public static final String SERVER_PARAM_KEYWORD = "keyword";
	
	public static final String SERVER_DATE_MODE = "date";
	public static final String SERVER_DISTANCE_MODE = "distance";
	public static final String SERVER_HOTNESS_MODE = "hotness";
	public static final String SERVER_SEARCHING_MODE = "searching";
	
	public static final String SERVER_CATEGORY_ALL = "all";
	public static final String SERVER_CATEGORY_NEWS = "news";
	public static final String SERVER_CATEGORY_GIRLS = "girls";
	public static final String SERVER_CATEGORY_BOYS = "boys";
	public static final String SERVER_CATEGORY_POLITICS = "politics";
	public static final String SERVER_CATEGORY_SPORT = "sport";
	public static final String SERVER_CATEGORY_FUN = "fun";
	public static final String SERVER_CATEGORY_OFFER = "offer";
	
	public static final String CATEGORY_ALL = SERVER_CATEGORY_ALL;
	public static final String CATEGORY_NEWS = SERVER_CATEGORY_NEWS;
	public static final String CATEGORY_GIRLS = SERVER_CATEGORY_GIRLS;
	public static final String CATEGORY_BOYS = SERVER_CATEGORY_BOYS;
	public static final String CATEGORY_POLITICS = SERVER_CATEGORY_POLITICS;
	public static final String CATEGORY_SPORT = SERVER_CATEGORY_SPORT;
	public static final String CATEGORY_FUN = SERVER_CATEGORY_FUN;
	public static final String CATEGORY_OFFER = SERVER_CATEGORY_OFFER;
	public static final String CATEGORY_LATER= "read_later";
	
	/*
	 * URL constants go here
	 */
	public static String NEWS_IMAGE_URL = "";
	public static String USER_IMAGE_URL = "";
	public static String NEWS_URL = "";
	public static String USER_URL = "";
	public static String COMMENT_URL = "";
	
	
	/*
	 * Constants for JSON responses; the followings are the fields in MEWS object
	 * private int id;
	 * private String title;
	 * private String author;
	 * private Date date;
	 * private String content;
	 * private long lat;
	 * private long lng;
	 * private String link;
	 * private int viewCount;
	 * private String address;
	 * private String category;
	 * private int upvote;
	 * private int downvote;
	 * private String authorId;
	 * private String pic1;
	 * private String pic2;
	 * private String pic3;
	 */
	public static String JSON_TAG_ID = "id";
	public static String JSON_TAG_TITLE = "title";
	public static String JSON_TAG_AUTHOR = "author";
	public static String JSON_TAG_DATE = "date";
	public static String JSON_TAG_CONTENT = "content";
	public static String JSON_TAG_LAT = "lat";
	public static String JSON_TAG_LNG = "lng";
	public static String JSON_TAG_LINK = "link";
	public static String JSON_TAG_VIEWCOUNT = "view_count";
	public static String JSON_TAG_CATEGORY = "category";
	public static String JSON_TAG_ADDRESS = "address";
	public static String JSON_TAG_UPVOTE = "upvote";
	public static String JSON_TAG_DOWNVOTE = "downvote";
	public static String JSON_TAG_AUTHORID = "author_id";
	public static String JSON_TAG_THUMBNAIL = "thumbnail";
	public static String JSON_TAG_PIC1 = "pic_1";
	public static String JSON_TAG_PIC2 = "pic_2";
	public static String JSON_TAG_PIC3 = "pic_3";

	public static final String DOUBLE_QOUTE = "\"";
	public static final String JSON_OPENFIRST = "[";
	public static final String JSON_OPEN = "{";
	public static final String JSON_END = "}";
	public static final String JSON_END_LAST = "]";
	public static final String JSON_COLON = ":";
	public static final String COMMA = ",";
	
	public static final String TESTING_JSON = JSON_OPENFIRST+
			JSON_OPEN+
			DOUBLE_QOUTE+JSON_TAG_ID+DOUBLE_QOUTE+JSON_COLON+"1"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_TITLE+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"湮�珩�ˋ郳婈霜檢漇毧﹛笒恉類3苤"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_AUTHOR+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"Nownews 踏陔�"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_DATE+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"Thu, 27 Feb 2014 23:10:00"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_CONTENT+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"湮�碩鰍笢笣繚湮啞毞珩堤政珨躉劓ㄛ珨靡嬴郳笢爛鹹赽旻婓醪繚煦路笢栝ㄛ奧珨靡霜檢萱旯婓奻ㄛ嫖毞趙眳狟忒扥筳嬴郳鹹赽�洖ㄛ苤陑秫秫腔笒恉翍稛靡郳ㄛ旮鷓參稛毞奻裁狟腔跎昜緻讀倳ㄛ稛盟劓柲竘湮蠶�罠ㄛ�衄繚鼴狟桽奻鋒繚ㄛ蕾撈掩�遼滖徭ㄛ衄鋒衭隱晟想ㄛ稛褫岆☆爛僅郔槽賭嬴惿★﹝跦湮�羸闚笢鋒ㄛ掛堎23黺鰍�笢笣繚鰍坒嫢埏蜇輪ㄛ硐�湮醪繚奻旻翍珨靡嬴郳網網湮阯腔鹹赽ㄛ侔綱掩霜檢☆★漇毧ㄛ衄鋒衭參稛躉劓砉鼴狟奻鋒繚ㄛ稛靡霜檢狟敁2珨眻笒恉﹜類郳善5偝墿絻3�苤ㄛ朼祫嫖毞趙眳狟忒扥筳郳�洖奻狟忒﹝硌堤ㄛ桽奻鋒繚摽蕾撈柲竘湮蠶鋒衭隱晟睿�遼滖徭ㄛ衄鋒衭桶尨ㄛ淩砑眭耋稛靡郳嬴倳摽艘善赻撩掩霜檢☆★腔毀ㄛ珩衄鋒衭想稛泃褫岆☆爛僅郔槽賭嬴惿★�衄鋒衭挲ㄛ淩腔陑賭嬴賸﹝"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_LAT+DOUBLE_QOUTE+JSON_COLON+"34.435622"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_LNG+DOUBLE_QOUTE+JSON_COLON+"115.669837"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_LINK+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"http://hk.news.yahoo.com/%E5%A4%A7%E9%99%B8%E4%B9%9F%E7%98%8B%E6%92%BF%E5%B1%8D-%E9%86%89%E6%BC%A2%E6%85%98%E9%81%AD%E6%B5%81%E6%B5%AA%E6%BC%A2%E7%8C%A5%E8%A4%BB-%E8%A6%AA%E5%90%BB%E6%92%AB%E6%91%B83%E5%B0%8F%E6%99%82-040001466.html"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_VIEWCOUNT+DOUBLE_QOUTE+JSON_COLON+"509"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_CATEGORY+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"news"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_ADDRESS+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"碩鰍笢笣繚"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_UPVOTE+DOUBLE_QOUTE+JSON_COLON+"345"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_DOWNVOTE+DOUBLE_QOUTE+JSON_COLON+"12"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_AUTHORID+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"https://socialprofiles.zenfs.com/images/d407d01ee164a6c0fd55180bc1cfd3e5_48.png"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_THUMBNAIL+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"http://l.yimg.com/mq/i/nwl/mntl/pha1840000101.jpg"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_PIC1+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"http://farm2.staticflickr.com/1044/548083050_30269e39b4_b.jpg"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_PIC2+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_PIC3+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+DOUBLE_QOUTE+
			JSON_END+
			JSON_END_LAST
			;
	
	public static final String TESTING_JSON2 = JSON_OPENFIRST+
			JSON_OPEN+
			DOUBLE_QOUTE+JSON_TAG_ID+DOUBLE_QOUTE+JSON_COLON+"2"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_TITLE+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"想☆�璵操彴��昜★﹛荎鹹婈�虛皺旯輦蛂"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_AUTHOR+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"藩赩"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_DATE+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"Thu, 27 Feb 2014 23:10:00"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_CONTENT+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"蕣笢陑ㄞ遨磁蚅蘇�岆痸ˋㄐ荎珨靡35鹹赽驚塋(Jason Payne)ㄛ妏蚚鋒繚�滇炵緙蕼��華珨模洷�蕺�虛(Hilton Hotel)ㄛ祥綎坻婓沓�桶跡笢腔�昜弇ㄛ秪珨鶸�賸☆扂�洖衄珨操彴★ㄛ磐彆掩��虛蹈皺旯擇稷厘﹝跦荎▲藩赩◎(Daily Mail)ㄛ��闚鼠侗��腔驚塋ㄛ珂婓鋒繚�滇炵緙奻腔揃蹋奻ㄛ赻眕蚅蘇腔沓�賸☆扂�洖璵賸珨操湮腔彴ㄛ稛岆扂峔珨腔�昜★﹝奧�驚塋厘�虛��煇燴蛂忒鴦ㄛ諉渾艘善�奻揃蹋摽�祥蛂遼虷ㄛ�俀坻珩�瞳隱咑﹝祥綎ㄛ眳摽驚塋彶善珨猾赻�虛�燴垀追冞腔�赩﹝笢硌堤ㄛ旯翋奪ㄛ祥洷咡�肮忳善晟惤奻腔礝ㄛ☆�洖衄操彴★稛�虷�珨飲祥蚅蘇ㄛ���痸ㄛ秪森隅坻蹈擇稷厘ㄛ皺旯楊蛂��虛﹝森ㄛ驚塋庲��虛�燴怮綎苤觳湮釬ㄛ椰蚅蘇覜﹝"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_LAT+DOUBLE_QOUTE+JSON_COLON+"34.435622"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_LNG+DOUBLE_QOUTE+JSON_COLON+"115.669837"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_LINK+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"http://hk.news.yahoo.com/%E5%A4%A7%E9%99%B8%E4%B9%9F%E7%98%8B%E6%92%BF%E5%B1%8D-%E9%86%89%E6%BC%A2%E6%85%98%E9%81%AD%E6%B5%81%E6%B5%AA%E6%BC%A2%E7%8C%A5%E8%A4%BB-%E8%A6%AA%E5%90%BB%E6%92%AB%E6%91%B83%E5%B0%8F%E6%99%82-040001466.html"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_VIEWCOUNT+DOUBLE_QOUTE+JSON_COLON+"809"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_CATEGORY+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"news"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_ADDRESS+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"洷�蕺�虛(Hilton Hotel)"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_UPVOTE+DOUBLE_QOUTE+JSON_COLON+"325"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_DOWNVOTE+DOUBLE_QOUTE+JSON_COLON+"111"+COMMA+
			DOUBLE_QOUTE+JSON_TAG_AUTHORID+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"https://socialprofiles.zenfs.com/images/d407d01ee164a6c0fd55180bc1cfd3e5_48.png"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_THUMBNAIL+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"http://www.people.com.cn/mediafile/pic/20140124/70/275881191046948490.jpg"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_PIC1+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+"http://farm2.staticflickr.com/1044/548083050_30269e39b4_b.jpg"+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_PIC2+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+DOUBLE_QOUTE+COMMA+
			DOUBLE_QOUTE+JSON_TAG_PIC3+DOUBLE_QOUTE+JSON_COLON+DOUBLE_QOUTE+DOUBLE_QOUTE+
			JSON_END+
			JSON_END_LAST
			;

	
	public static MewsList convertFastJSON(List<Map<String, Object>> list){
		MewsList mewsList = new MewsList();
		for(int i=0;i<list.size();i++){
			Mews mews = new Mews();
			try{
				mews.setId(Integer.valueOf(list.get(i).get(JSON_TAG_ID).toString()));
				mews.setTitle(list.get(i).get(JSON_TAG_TITLE).toString());
				mews.setAuthor(list.get(i).get(JSON_TAG_AUTHOR).toString());
				mews.setDate(list.get(i).get(JSON_TAG_DATE).toString());
				mews.setContent(list.get(i).get(JSON_TAG_CONTENT).toString());
				mews.setLatitude(Double.valueOf(list.get(i).get(JSON_TAG_LAT).toString()));
				mews.setLongitude(Double.valueOf(list.get(i).get(JSON_TAG_LNG).toString()));
				mews.setLink(list.get(i).get(JSON_TAG_LINK).toString());
				mews.setCategory(list.get(i).get(JSON_TAG_CATEGORY).toString());
				mews.setAddress(list.get(i).get(JSON_TAG_ADDRESS).toString());
				mews.setVote(Integer.valueOf(list.get(i).get(JSON_TAG_UPVOTE).toString()), Integer.valueOf(list.get(i).get(JSON_TAG_DOWNVOTE).toString()));
				mews.setAuthorId(list.get(i).get(JSON_TAG_AUTHORID).toString());
				mews.setThumbnail(list.get(i).get(JSON_TAG_THUMBNAIL).toString());
				mews.setPic1(list.get(i).get(JSON_TAG_PIC1).toString());
				mews.setPic2(list.get(i).get(JSON_TAG_PIC2).toString());
				mews.setPic3(list.get(i).get(JSON_TAG_PIC3).toString());
			}catch(Exception e){
				Log.i(TAG, TAG+" Error occur when get data from list: "+e);
			}
			mewsList.addItemToEnd(mews);
		}
		return mewsList;		
	}
	
	/*
	 * In-app constants
	 */
	public static final String APP_NAME = "Mews";
	public static final String CACHE_FILE = "mews_cache";
	public static final String IMAGE_FILE = "mews_image";
	public static final String OFFLINE_FILE = "offline_reading";
	public static final String FLURRY_API_KEY = "NZXVSGRJJ447VP8N6TJN";
	
	public static final String BUNDLE_POSITION = "NZXVSGRJJ447VP8N6TJN";
	public static final String BUNDLE_ = "NZXVSGRJJ447VP8N6TJN";
	public static final String BUNDLE_POS = "NZXVSGRJJ447VP8N6TJN";
	//SharedPreferences Constants
	public static final String PREFERENCE_NAME_LOGIN = "login";
	public static final String PREFERENCE_LOGGED_IN = "logged_in";
	public static final String PREFERENCE_USERNAME = "username";
	public static final String PREFERENCE_USERID = "user_id";
	public static final String PREFERENCE_ACCESS_TOKEN = "access_token";
	public static final String PREFERENCE_LOGIN_EXPIRE_TIME = "login_expire_time";
	
	public static final String PREFERENCE_SORTING = "sorting";

	/*
	 * Flurry Parameters
	 */
	public static final String FLURRY_EVENT_DETAIL = "event_detail";
	/*
	 * In-app methods 
	 */
	public static void updateUnread(DbAdapter myDbAdapter, TextView unread_textview){
		
		myDbAdapter.open();
		int no_of_unread = myDbAdapter.getNumberOfUnread();
		myDbAdapter.close();

		if(no_of_unread>0){
			unread_textview.setVisibility(View.VISIBLE);
			unread_textview.setText(String.valueOf(no_of_unread));
		}else{
			unread_textview.setVisibility(View.GONE);
		}
	}
	/*
	 * Constants for user
	 */
}
