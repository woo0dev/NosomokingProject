package com.example.somok;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.content.Intent;

public class CalendarActivity extends Activity {

    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate;
    /**
     * 그리드뷰 어댑터
     */
    private GridAdapter gridAdapter;

    /**
     * 일 저장 할 리스트
     */
    private ArrayList<String> dayList;

    /**
     * 그리드뷰
     */
    private GridView gridView;

    /**
     * 캘린더 변수
     */
    public Calendar mCal;
    static int score;
    int money = 135000;
    int a;
    ArrayList pos = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);

        tvDate = findViewById(R.id.tv_date);
        gridView = findViewById(R.id.gridview);
        final TextView scoreView = findViewById(R.id.s);
        final TextView moneyView = findViewById(R.id.money);
        final Button back = findViewById(R.id.back);

        // 클릭이벤트
        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 포지션 클릭 이벤트

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "position: " + (position-9), Toast.LENGTH_SHORT).show();
            }
        });*/

        SharedPreferences spf = getSharedPreferences("key", MODE_PRIVATE);
        int spScore = spf.getInt("key", score);
        score = spScore;
        money = score * 4500;

        scoreView.setText(String.format("이번달 흡연한 횟수 : %d", score));
        moneyView.setText(String.format("이번달 담배 소비 금액 : %d원", money));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 포지션 클릭 이벤트
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                score++;
                view.setBackgroundColor(getResources().getColor(R.color.color_ff0000));
                pos.add(position);
                scoreView.setText(String.format("이번달 흡연한 횟수 : %d", score));
                money += 4500;
                moneyView.setText(String.format("이번달 담배 소비 금액 : %d원", money));
                CalendarLogin.score = score;
                onStop();
            }
        });



        Button leftBtn = findViewById(R.id.leftBtn);
        Button rightBtn = findViewById(R.id.rightBtn);

        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("M", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        final int[] year = {Integer.parseInt(curYearFormat.format(date))};
        final int[] day = {Integer.parseInt(curMonthFormat.format(date))};

        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

        //gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();

        final int[] my = {mCal.get(Calendar.MONTH) + 1};

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(year[0], day[0] - 1, 1);
        final int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(my[0]);

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);



        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dayList.removeAll(dayList);
                dayList.add("일");
                dayList.add("월");
                dayList.add("화");
                dayList.add("수");
                dayList.add("목");
                dayList.add("금");
                dayList.add("토");
                mCal = Calendar.getInstance();
                mCal.set(year[0], day[0], 1);
                if (day[0] < 12) {
                    day[0]++;
                } else {
                    year[0]++;
                    day[0] = 1;
                }
                tvDate.setText(year[0] + "/" + day[0]);
                int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
 //1일 - 요일 매칭 시키기 위해 공백 add
                for (int i = 1; i < dayNum; i++) {
                    dayList.add("");
                }
                setCalendarDate(my[0] + 1);
                gridAdapter.notifyDataSetChanged();
            }
        });

        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dayList.removeAll(dayList);
                dayList.add("일");
                dayList.add("월");
                dayList.add("화");
                dayList.add("수");
                dayList.add("목");
                dayList.add("금");
                dayList.add("토");
                mCal = Calendar.getInstance();
                mCal.set(year[0], day[0] - 1 -1, 1);
                if (day[0] > 1) {
                    day[0]--;
                } else {
                    year[0]--;
                    day[0] = 12;
                }
                tvDate.setText(year[0] + "/" + day[0]);
                int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
 //1일 - 요일 매칭 시키기 위해 공백 add
                for (int i = 1; i < dayNum; i++) {
                    dayList.add("");
                }
                setCalendarDate(my[0] - 1);
                gridAdapter.notifyDataSetChanged();
            }
        });

        final Intent intent = new Intent(this.getIntent());

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(), CalendarLogin.class);
                startActivity(backIntent);
            }
        });
    }

    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param month
     */
    private void setCalendarDate(int month) {
        //mCal.set(Calendar.MONTH, month - 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }

    }

    /**
     * 그리드뷰 어댑터
     */
    protected class GridAdapter extends BaseAdapter {

        private final List<String> list;

        private final LayoutInflater inflater;

        /**
         * 생성자
         *
         * @param context
         * @param list
         */
        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
                                                     return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));
            //holder.tvItemGridView.setBackgroundColor(getResources().getColor(R.color.color_ff0000));

            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            /*if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.color_000000));
            }*/

            return convertView;
        }


    }

    private class ViewHolder {
        TextView tvItemGridView;
    }

    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("key", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int spScore = score;
        editor.putInt("key", spScore);
        editor.commit();
    }
}