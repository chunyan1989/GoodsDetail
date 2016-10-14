package com.zcy.goodsdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcy.goodsdetail.fragment.CommentFragment;
import com.zcy.goodsdetail.fragment.ContentDetailFragment;
import com.zcy.goodsdetail.fragment.GoodsDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品详情界面
 * Created by zhangchunyan on 16/10/11.
 */
public class GoodsActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "GoodsActivity";

    @BindView(R.id.tv_goods)
    TextView tvGoods;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.lly_good)
    LinearLayout llyGood;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.lly_detail)
    LinearLayout llyDetail;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private List<Fragment> list;
    private MinePagerAdapter adapter;
    private GoodsDetailFragment goodsDetailFragment;
    private ContentDetailFragment contentDetailFragment;
    private CommentFragment commentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        goodsDetailFragment = GoodsDetailFragment.newInstance();
        contentDetailFragment = ContentDetailFragment.newInstance();
        commentFragment = CommentFragment.newInstance();
        list = new ArrayList<>();
        list.add(goodsDetailFragment);
        list.add(contentDetailFragment);
        list.add(commentFragment);
        adapter = new MinePagerAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);
        setColor();
        viewpager.setCurrentItem(0, false);
        tvGoods.setTextColor(getResources().getColor(R.color.colorAccent));
        line1.setVisibility(View.VISIBLE);
        viewpager.setOnPageChangeListener(this);
    }

    private void setColor() {
        tvGoods.setTextColor(getResources().getColor(R.color.colorPrimary));
        tvDetail.setTextColor(getResources().getColor(R.color.colorPrimary));
        tvComment.setTextColor(getResources().getColor(R.color.colorPrimary));
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
        line3.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setColor();
        switch (position) {
            case 0:
                tvGoods.setTextColor(getResources().getColor(R.color.colorAccent));
                line1.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvDetail.setTextColor(getResources().getColor(R.color.colorAccent));
                line2.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvComment.setTextColor(getResources().getColor(R.color.colorAccent));
                line3.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick({R.id.lly_good, R.id.lly_detail, R.id.lly_comment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lly_good:
                setColor();
                viewpager.setCurrentItem(0, false);
                tvGoods.setTextColor(getResources().getColor(R.color.colorAccent));
                line1.setVisibility(View.VISIBLE);
                break;
            case R.id.lly_detail:
                setColor();
                viewpager.setCurrentItem(1, false);
                tvDetail.setTextColor(getResources().getColor(R.color.colorAccent));
                line2.setVisibility(View.VISIBLE);
                break;
            case R.id.lly_comment:
                setColor();
                viewpager.setCurrentItem(2, false);
                tvComment.setTextColor(getResources().getColor(R.color.colorAccent));
                line3.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * ViewPager的PagerAdapter
     */
    public class MinePagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> list;

        public MinePagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
