package com.ivyiot.appsdk;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ivyiot.playback.CalendarUtils;
import com.ivyiot.playback.CustomDateCalendar;
import com.ivyiot.playback.DateAndTimeUtils;
import com.ivyiot.playback.DateLine;
import com.ivyiot.playback.EVideoType;
import com.ivyiot.playback.ICloudVideoPlayListener;
import com.ivyiot.playback.ITimeLineListener;
import com.ivyiot.playback.IvyCloudVideoView;
import com.ivyiot.playback.IvyVideo;
import com.ivyiot.playback.TimeLineView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CloudPlaybackActivity extends AppCompatActivity implements View.OnClickListener, ICloudVideoPlayListener {
    private final String TAG = "CloudPlaybackActivity";
    private IvyCloudVideoView videoView;

    private DateLine dateLine;

    private TimeLineView timeLine;

    private String path;
    private IvyVideo currIvyVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        path = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_adv_example_hevc/master.m3u8"; //apple h265 + fmp4
        path = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"; //apple h264 + fmp4
//        path = "https://plb-test.myfoscam.com:443/hls/04caa82ef0e22665d1016034fad67dd51044877A0E8AF14039D/2020/11/17/165742.m3u8?timerange=1605603462-1605603505&expires=1605607435&urlVersion=&sign=2d2a8a86b236525d609c2ed981c6d3df&storageType=30DAYS&oemCode=&se=1";
        setContentView(R.layout.cloud_play_back_view);
        videoView = findViewById(R.id.cloud_video_view);
//        cloudTimeview = findViewById(R.id.cloud_time_view);
        dateLine = findViewById(R.id.dl_history_data);
        timeLine = findViewById(R.id.timeline);

        videoView.setCloudVideoPlayListener(this);
        findViewById(R.id.btn_cloud_pause).setOnClickListener(this);
        findViewById(R.id.btn_cloud_resume).setOnClickListener(this);
        findViewById(R.id.btn_cloud_seek).setOnClickListener(this);
        findViewById(R.id.btn_cloud_audio_open).setOnClickListener(this);
        findViewById(R.id.btn_cloud_audio_close).setOnClickListener(this);
        videoView.startCloudVideo(path);

        dateLine.setChosenDay(CalendarUtils.getChoseDayIsOutDate());
        dateLine.setOnDateChangeListener(dateCalendar -> {
            Toast.makeText(this, "onDateChange date=" + dateCalendar.year + dateCalendar.month + dateCalendar.day, Toast.LENGTH_SHORT).show();
            videoView.stopPlayback();
            timeLine.setCloudVideoMap(null);
        });

        String dateJson = "{\"errorCode\":\"\",\"failureDetails\":\"\",\"dateList\":[\"20201116T150000\",\"20201117T020000\",\"20201117T070000\",\"20201117T050000\",\"20201116T070000\",\"20201117T080000\",\"20201116T060000\",\"20201117T010000\",\"20201116T050000\",\"20201117T060000\",\"20201116T100000\",\"20201116T170000\",\"20201116T180000\",\"20201116T140000\",\"20201116T080000\",\"20201116T120000\",\"20201116T110000\",\"20201116T090000\",\"20201116T130000\",\"20201116T160000\"]}";
        //dateLine.updateDateLine(dataJson,"2020-11-17");
        List<String> date_list = dateLine.analyseCloudDateData(dateJson);
        dateLine.setDataList(date_list);



        timeLine.setTimeLineListener(new ITimeLineListener() {
            @Override
            public void onSetCloudVideoColor(EVideoType type) {
                switch (type) {
                    case LIVE:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_timeline_have_cloud);
                        break;
                    case MOTION:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_time_line_bg_motion);
                        break;
                    case SOUND:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_time_line_bg_sound);
                        break;
                    case TEMPERATURE:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_time_line_bg_temperature);
                        break;
                    case HUMIDITY:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_time_line_bg_humidity);
                        break;
                    case IO:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_time_line_bg_io);
                        break;
                    case HUMAN:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_time_line_bg_human);
                        break;
                    case FACE:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_time_line_bg_face);
                        break;
                    case DoorBellLeave:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_time_line_bg_doorbellleave);
                        break;
                    default:
                        timeLine.setCloudVideoColor(com.ivyiot.playback.R.color.light_time_line_bg_other);
                        break;
                }
            }

            @Override
            public void onTimeScrollStart() {
                Log.d(TAG, "onTimeScrollStart");
            }

            @Override
            public void onTimeScrollMove(long time) {
                Log.d(TAG, "onTimeScrollMove:  " + time);
            }

            @Override
            public void onTimeScrollEnd(long time) {
                Log.d(TAG, "onTimeScrollEnd:  " + time);
                //videoView.startCloudVideo(path);
                CustomDateCalendar currDate = CalendarUtils.setChoseDayIsOutDate("2020-11-17");
                currIvyVideo = timeLine.convertToIvyVideo(time, currDate);
            }
        });
        int screenWidth = getScreenSize(this);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        timeLine.initViewParam(hour * 3600 + min * 60);
        String timeJson = "{\"errorCode\":\"\",\"failureDetails\":\"\",\"recordList\":[[\"1605541873\",\"1605542473\",\"0\"],[\"1605542473\",\"1605543073\",\"0\"],[\"1605543073\",\"1605543674\",\"0\"],[\"1605543674\",\"1605544274\",\"0\"],[\"1605544274\",\"1605544874\",\"0\"],[\"1605544874\",\"1605545475\",\"0\"],[\"1605545475\",\"1605546075\",\"0\"],[\"1605546075\",\"1605546675\",\"0\"],[\"1605546675\",\"1605547275\",\"0\"],[\"1605547275\",\"1605547876\",\"0\"],[\"1605547876\",\"1605548476\",\"0\"],[\"1605548476\",\"1605549076\",\"0\"],[\"1605549076\",\"1605549677\",\"0\"],[\"1605549677\",\"1605550277\",\"0\"],[\"1605550277\",\"1605550877\",\"0\"],[\"1605550877\",\"1605551478\",\"0\"],[\"1605551478\",\"1605552078\",\"0\"],[\"1605552078\",\"1605552678\",\"0\"],[\"1605552678\",\"1605553202\",\"2\"],[\"1605577298\",\"1605577303\",\"0\"],[\"1605577303\",\"1605577346\",\"2\"],[\"1605577346\",\"1605577349\",\"0\"],[\"1605577349\",\"1605577391\",\"2\"],[\"1605577391\",\"1605577406\",\"0\"],[\"1605577406\",\"1605577490\",\"3\"],[\"1605577490\",\"1605577494\",\"0\"],[\"1605577494\",\"1605577536\",\"2\"],[\"1605577536\",\"1605577549\",\"0\"],[\"1605577549\",\"1605577591\",\"2\"],[\"1605577591\",\"1605577634\",\"2\"],[\"1605577634\",\"1605577643\",\"0\"],[\"1605577643\",\"1605577685\",\"2\"],[\"1605577685\",\"1605577729\",\"3\"],[\"1605577729\",\"1605577733\",\"0\"],[\"1605577733\",\"1605577775\",\"3\"],[\"1605577775\",\"1605577780\",\"0\"],[\"1605577780\",\"1605577822\",\"2\"],[\"1605577822\",\"1605577864\",\"2\"],[\"1605577864\",\"1605577907\",\"12\"],[\"1605577907\",\"1605577916\",\"0\"],[\"1605577916\",\"1605577958\",\"2\"],[\"1605577958\",\"1605578001\",\"3\"],[\"1605578001\",\"1605578004\",\"0\"],[\"1605578004\",\"1605578046\",\"3\"],[\"1605578046\",\"1605578055\",\"0\"],[\"1605578055\",\"1605578097\",\"2\"],[\"1605578097\",\"1605578101\",\"0\"],[\"1605578101\",\"1605578143\",\"2\"],[\"1605578143\",\"1605578146\",\"0\"],[\"1605578146\",\"1605578187\",\"2\"],[\"1605578187\",\"1605578197\",\"0\"],[\"1605578197\",\"1605578240\",\"3\"],[\"1605578240\",\"1605578284\",\"3\"],[\"1605578284\",\"1605578287\",\"0\"],[\"1605578287\",\"1605578329\",\"3\"],[\"1605578329\",\"1605578371\",\"3\"],[\"1605578371\",\"1605578376\",\"0\"],[\"1605578376\",\"1605578418\",\"2\"],[\"1605578418\",\"1605578425\",\"0\"],[\"1605578425\",\"1605578467\",\"2\"],[\"1605578467\",\"1605578510\",\"2\"],[\"1605578510\",\"1605578553\",\"3\"],[\"1605578553\",\"1605578557\",\"0\"],[\"1605578557\",\"1605578599\",\"2\"],[\"1605578599\",\"1605578613\",\"0\"],[\"1605578613\",\"1605578655\",\"2\"],[\"1605578655\",\"1605578676\",\"0\"],[\"1605578676\",\"1605578719\",\"2\"],[\"1605578719\",\"1605578724\",\"0\"],[\"1605578724\",\"1605578766\",\"2\"],[\"1605578766\",\"1605578809\",\"2\"],[\"1605578809\",\"1605578816\",\"0\"],[\"1605578816\",\"1605578858\",\"3\"],[\"1605578858\",\"1605578862\",\"0\"],[\"1605578862\",\"1605578904\",\"3\"],[\"1605578904\",\"1605578908\",\"0\"],[\"1605578908\",\"1605578950\",\"2\"],[\"1605578950\",\"1605578953\",\"0\"],[\"1605578953\",\"1605578995\",\"2\"],[\"1605578995\",\"1605579048\",\"0\"],[\"1605579048\",\"1605579090\",\"2\"],[\"1605579090\",\"1605579097\",\"0\"],[\"1605579097\",\"1605579140\",\"2\"],[\"1605579140\",\"1605579144\",\"0\"],[\"1605579144\",\"1605579187\",\"3\"],[\"1605579187\",\"1605579191\",\"0\"],[\"1605579191\",\"1605579233\",\"2\"],[\"1605579233\",\"1605579236\",\"0\"],[\"1605579236\",\"1605579279\",\"2\"],[\"1605579279\",\"1605579285\",\"0\"],[\"1605579285\",\"1605579327\",\"2\"],[\"1605579327\",\"1605579336\",\"0\"],[\"1605579336\",\"1605579379\",\"2\"],[\"1605579379\",\"1605579386\",\"0\"],[\"1605579386\",\"1605579428\",\"2\"],[\"1605579428\",\"1605579433\",\"0\"],[\"1605579433\",\"1605579474\",\"2\"],[\"1605579474\",\"1605579494\",\"0\"],[\"1605579494\",\"1605579537\",\"2\"],[\"1605579537\",\"1605579562\",\"0\"],[\"1605579562\",\"1605579604\",\"2\"],[\"1605579604\",\"1605579622\",\"0\"],[\"1605579622\",\"1605579663\",\"2\"],[\"1605579663\",\"1605579677\",\"0\"],[\"1605579677\",\"1605579718\",\"2\"],[\"1605579718\",\"1605579745\",\"0\"],[\"1605579745\",\"1605579788\",\"2\"],[\"1605579788\",\"1605579802\",\"0\"],[\"1605579802\",\"1605579843\",\"2\"],[\"1605579843\",\"1605579848\",\"0\"],[\"1605579848\",\"1605579889\",\"2\"],[\"1605579889\",\"1605579915\",\"0\"],[\"1605579915\",\"1605579957\",\"2\"],[\"1605579957\",\"1605579983\",\"0\"],[\"1605579983\",\"1605580025\",\"2\"],[\"1605580025\",\"1605580034\",\"0\"],[\"1605580034\",\"1605580076\",\"2\"],[\"1605580076\",\"1605580094\",\"0\"],[\"1605580094\",\"1605580136\",\"2\"],[\"1605580136\",\"1605580142\",\"0\"],[\"1605580142\",\"1605580183\",\"2\"],[\"1605580183\",\"1605580197\",\"0\"],[\"1605580197\",\"1605580238\",\"2\"],[\"1605580238\",\"1605580242\",\"0\"],[\"1605580242\",\"1605580285\",\"2\"],[\"1605580285\",\"1605580299\",\"0\"],[\"1605580299\",\"1605580340\",\"2\"],[\"1605580340\",\"1605580361\",\"0\"],[\"1605580361\",\"1605580403\",\"3\"],[\"1605580403\",\"1605580417\",\"0\"],[\"1605580417\",\"1605580459\",\"3\"],[\"1605580459\",\"1605580478\",\"0\"],[\"1605580478\",\"1605580519\",\"3\"],[\"1605580519\",\"1605580570\",\"0\"],[\"1605580570\",\"1605580611\",\"3\"],[\"1605580611\",\"1605580615\",\"0\"],[\"1605580615\",\"1605580657\",\"3\"],[\"1605580657\",\"1605580794\",\"0\"],[\"1605580794\",\"1605580836\",\"3\"],[\"1605580836\",\"1605580874\",\"0\"],[\"1605580874\",\"1605580916\",\"3\"],[\"1605580916\",\"1605580946\",\"0\"],[\"1605580946\",\"1605580988\",\"3\"],[\"1605580988\",\"1605580991\",\"0\"],[\"1605580991\",\"1605581033\",\"2\"],[\"1605581033\",\"1605581037\",\"0\"],[\"1605581037\",\"1605581079\",\"3\"],[\"1605581079\",\"1605581099\",\"0\"],[\"1605581099\",\"1605581140\",\"3\"],[\"1605581140\",\"1605581176\",\"0\"],[\"1605581176\",\"1605581218\",\"3\"],[\"1605581218\",\"1605581228\",\"0\"],[\"1605581228\",\"1605581270\",\"3\"],[\"1605581270\",\"1605581313\",\"3\"],[\"1605581313\",\"1605581319\",\"0\"],[\"1605581319\",\"1605581361\",\"3\"],[\"1605581361\",\"1605581406\",\"3\"],[\"1605592744\",\"1605592903\",\"0\"],[\"1605592903\",\"1605592945\",\"2\"],[\"1605592945\",\"1605592988\",\"2\"],[\"1605592988\",\"1605592998\",\"0\"],[\"1605592998\",\"1605593040\",\"2\"],[\"1605593040\",\"1605593048\",\"0\"],[\"1605593048\",\"1605593090\",\"2\"],[\"1605593090\",\"1605593103\",\"0\"],[\"1605593103\",\"1605593145\",\"2\"],[\"1605593145\",\"1605593158\",\"0\"],[\"1605593158\",\"1605593202\",\"2\"],[\"1605593202\",\"1605593252\",\"0\"],[\"1605593252\",\"1605593294\",\"2\"],[\"1605593294\",\"1605593335\",\"0\"],[\"1605593335\",\"1605593360\",\"2\"],[\"1605593440\",\"1605593477\",\"2\"],[\"1605593477\",\"1605593496\",\"0\"],[\"1605593496\",\"1605593582\",\"2\"],[\"1605593582\",\"1605593610\",\"0\"],[\"1605593610\",\"1605593652\",\"2\"],[\"1605593652\",\"1605593703\",\"0\"],[\"1605593703\",\"1605593745\",\"2\"],[\"1605593745\",\"1605593761\",\"0\"],[\"1605593761\",\"1605593803\",\"2\"],[\"1605593803\",\"1605593830\",\"0\"],[\"1605593830\",\"1605593873\",\"2\"],[\"1605593873\",\"1605593916\",\"2\"],[\"1605593916\",\"1605593989\",\"0\"],[\"1605593989\",\"1605594031\",\"2\"],[\"1605594031\",\"1605594097\",\"2\"],[\"1605594241\",\"1605594464\",\"0\"],[\"1605594464\",\"1605594506\",\"2\"],[\"1605594506\",\"1605594528\",\"0\"],[\"1605594528\",\"1605594571\",\"2\"],[\"1605594571\",\"1605594591\",\"0\"],[\"1605594591\",\"1605594633\",\"2\"],[\"1605594633\",\"1605594642\",\"0\"],[\"1605594728\",\"1605594756\",\"0\"],[\"1605594756\",\"1605594799\",\"2\"],[\"1605594799\",\"1605594805\",\"0\"],[\"1605594805\",\"1605594847\",\"2\"],[\"1605594847\",\"1605594855\",\"0\"],[\"1605594855\",\"1605594897\",\"2\"],[\"1605594897\",\"1605594975\",\"0\"],[\"1605594975\",\"1605595018\",\"2\"],[\"1605595018\",\"1605595056\",\"0\"],[\"1605595056\",\"1605595098\",\"2\"],[\"1605595098\",\"1605595101\",\"0\"],[\"1605595101\",\"1605595143\",\"2\"],[\"1605595143\",\"1605595155\",\"0\"],[\"1605595155\",\"1605595197\",\"2\"],[\"1605595197\",\"1605595241\",\"2\"],[\"1605595241\",\"1605595246\",\"0\"],[\"1605595246\",\"1605595289\",\"2\"],[\"1605595289\",\"1605595302\",\"0\"],[\"1605595302\",\"1605595345\",\"2\"],[\"1605595345\",\"1605595390\",\"0\"],[\"1605595390\",\"1605595431\",\"2\"],[\"1605595431\",\"1605595436\",\"0\"],[\"1605595436\",\"1605595479\",\"2\"],[\"1605595479\",\"1605595490\",\"0\"],[\"1605595490\",\"1605595532\",\"2\"],[\"1605595532\",\"1605595539\",\"0\"],[\"1605595539\",\"1605595581\",\"2\"],[\"1605595581\",\"1605595591\",\"0\"],[\"1605595591\",\"1605595633\",\"2\"],[\"1605595633\",\"1605595662\",\"0\"],[\"1605595662\",\"1605595704\",\"2\"],[\"1605595704\",\"1605595715\",\"0\"],[\"1605595715\",\"1605595757\",\"2\"],[\"1605595757\",\"1605595824\",\"0\"],[\"1605595824\",\"1605595866\",\"2\"],[\"1605595866\",\"1605595871\",\"0\"],[\"1605595871\",\"1605595913\",\"2\"],[\"1605595913\",\"1605595926\",\"0\"],[\"1605595926\",\"1605595968\",\"2\"],[\"1605595968\",\"1605596013\",\"2\"],[\"1605596013\",\"1605596019\",\"0\"],[\"1605596019\",\"1605596062\",\"2\"],[\"1605596062\",\"1605596121\",\"0\"],[\"1605596121\",\"1605596164\",\"2\"],[\"1605596164\",\"1605596168\",\"0\"],[\"1605596168\",\"1605596210\",\"2\"],[\"1605596210\",\"1605596233\",\"0\"],[\"1605596233\",\"1605596275\",\"2\"],[\"1605596275\",\"1605596282\",\"0\"],[\"1605596282\",\"1605596324\",\"2\"],[\"1605596324\",\"1605596336\",\"0\"],[\"1605596336\",\"1605596379\",\"2\"],[\"1605596379\",\"1605596421\",\"2\"],[\"1605596421\",\"1605596427\",\"0\"],[\"1605596427\",\"1605596469\",\"2\"],[\"1605596469\",\"1605596516\",\"0\"],[\"1605596516\",\"1605596558\",\"2\"],[\"1605596558\",\"1605596584\",\"0\"],[\"1605596584\",\"1605596626\",\"2\"],[\"1605596626\",\"1605596661\",\"0\"],[\"1605596661\",\"1605596702\",\"2\"],[\"1605596702\",\"1605596719\",\"0\"],[\"1605596719\",\"1605596760\",\"2\"],[\"1605596760\",\"1605596804\",\"2\"],[\"1605596804\",\"1605596817\",\"0\"],[\"1605596817\",\"1605596902\",\"2\"],[\"1605596902\",\"1605596953\",\"0\"],[\"1605596953\",\"1605596995\",\"2\"],[\"1605596995\",\"1605597038\",\"2\"],[\"1605597038\",\"1605597046\",\"0\"],[\"1605597046\",\"1605597088\",\"2\"],[\"1605597088\",\"1605597132\",\"2\"],[\"1605597132\",\"1605597143\",\"0\"],[\"1605597143\",\"1605597186\",\"2\"],[\"1605597186\",\"1605597209\",\"0\"],[\"1605597209\",\"1605597252\",\"2\"],[\"1605597252\",\"1605597264\",\"0\"],[\"1605597264\",\"1605597306\",\"2\"],[\"1605597306\",\"1605597436\",\"0\"],[\"1605597436\",\"1605597478\",\"2\"],[\"1605597478\",\"1605597509\",\"0\"],[\"1605597509\",\"1605597551\",\"2\"],[\"1605597551\",\"1605597576\",\"0\"],[\"1605597576\",\"1605597618\",\"2\"],[\"1605597618\",\"1605597635\",\"0\"],[\"1605597635\",\"1605597677\",\"2\"],[\"1605597677\",\"1605597700\",\"0\"],[\"1605597700\",\"1605597742\",\"2\"],[\"1605597742\",\"1605598268\",\"0\"],[\"1605598268\",\"1605598310\",\"2\"],[\"1605598310\",\"1605598350\",\"2\"],[\"1605598398\",\"1605598888\",\"0\"],[\"1605598888\",\"1605598930\",\"11\"],[\"1605598930\",\"1605598940\",\"0\"],[\"1605598940\",\"1605598982\",\"11\"],[\"1605598982\",\"1605599115\",\"0\"],[\"1605599115\",\"1605599157\",\"11\"],[\"1605599157\",\"1605599170\",\"0\"],[\"1605599170\",\"1605599212\",\"11\"],[\"1605599212\",\"1605599221\",\"0\"],[\"1605599221\",\"1605599263\",\"11\"],[\"1605599263\",\"1605599273\",\"0\"],[\"1605599273\",\"1605599315\",\"11\"],[\"1605599315\",\"1605599379\",\"0\"],[\"1605599379\",\"1605599421\",\"11\"],[\"1605599421\",\"1605599425\",\"0\"],[\"1605599425\",\"1605599467\",\"11\"],[\"1605599467\",\"1605599576\",\"0\"],[\"1605599576\",\"1605599605\",\"11\"],[\"1605602008\",\"1605602019\",\"0\"],[\"1605602019\",\"1605602061\",\"2\"],[\"1605602061\",\"1605602064\",\"0\"],[\"1605602064\",\"1605602105\",\"2\"],[\"1605602105\",\"1605602149\",\"2\"],[\"1605602149\",\"1605602159\",\"0\"],[\"1605602159\",\"1605602201\",\"2\"],[\"1605602201\",\"1605602264\",\"0\"],[\"1605602264\",\"1605602307\",\"2\"],[\"1605602307\",\"1605602331\",\"0\"],[\"1605602331\",\"1605602373\",\"2\"],[\"1605602373\",\"1605602459\",\"0\"],[\"1605602459\",\"1605602501\",\"2\"],[\"1605602501\",\"1605602537\",\"0\"],[\"1605602537\",\"1605602579\",\"3\"],[\"1605602579\",\"1605602585\",\"0\"],[\"1605602585\",\"1605602627\",\"3\"],[\"1605602627\",\"1605602630\",\"0\"],[\"1605602630\",\"1605602672\",\"3\"],[\"1605602672\",\"1605602715\",\"0\"],[\"1605602715\",\"1605602757\",\"2\"],[\"1605603327\",\"1605603370\",\"11\"],[\"1605603370\",\"1605603462\",\"0\"],[\"1605603462\",\"1605603505\",\"11\"]]}";
        CustomDateCalendar currDate = CalendarUtils.setChoseDayIsOutDate("2020-11-17");
        long mStartTime = DateAndTimeUtils.dateToUTC(currDate.year, (currDate.month-1), currDate.day);//要过滤当天的最大时间
        List<IvyVideo> cloudList = timeLine.analyseCloudTimeLineData(timeJson, mStartTime);
        HashMap<String, List<IvyVideo>> cloudListMap =  timeLine.analyseRecodeList(cloudList);
        timeLine.setTimeLineData(cloudListMap, cloudList);
        currIvyVideo = timeLine.convertToIvyVideo(timeLine.getValue(), currDate);
//        timeLine.setCloudVideoMap(null);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cloud_pause:
                videoView.pauseCloudVideo();
                break;
            case R.id.btn_cloud_resume:
                videoView.resumeCloudVideo();
                break;
            case R.id.btn_cloud_seek:
                videoView.seekTo(100);
                break;
            case R.id.btn_cloud_audio_open:
                videoView.setSoundSwitch(true);
                break;
            case R.id.btn_cloud_audio_close:
                videoView.setSoundSwitch(false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }

    /**
     * 获取设备屏幕宽度
     *
     * @param activity activity
     * @return
     */
    public static int getScreenSize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public void onStartPlay() {
        Log.d(TAG, "onStartPlay:  " );
        if(null != currIvyVideo){
            timeLine.minToMiddle(currIvyVideo.getStartTime());//缩放到最小时开始播放时恢复到正常
        }

    }


    @Override
    public void onPlaying(int position, int duration) {
        Log.d(TAG, "onPlaying:  " + position );
        timeLine.updateTimeLine(position, duration);
    }

    @Override
    public void onCompletion() {
        Log.d(TAG, "onCompletion:  "  );
    }

    @Override
    public void onError() {
        Log.d(TAG, "onError:  "  );
    }


}
