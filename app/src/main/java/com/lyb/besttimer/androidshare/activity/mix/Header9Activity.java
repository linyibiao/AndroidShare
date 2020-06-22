package com.lyb.besttimer.androidshare.activity.mix;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.complex.Headers9View;

import java.util.Arrays;

public class Header9Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header9);
        Headers9View headers9View = findViewById(R.id.header);
        headers9View.setHeaders(Arrays.asList("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572257755806&di=499d8ab1b99439f92ddc19d32744337d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F04%2F20131204184148_hhXUT.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572257755806&di=499d8ab1b99439f92ddc19d32744337d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F04%2F20131204184148_hhXUT.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572257755806&di=499d8ab1b99439f92ddc19d32744337d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F04%2F20131204184148_hhXUT.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572257755806&di=499d8ab1b99439f92ddc19d32744337d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F04%2F20131204184148_hhXUT.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572257755806&di=499d8ab1b99439f92ddc19d32744337d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F04%2F20131204184148_hhXUT.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572257755806&di=499d8ab1b99439f92ddc19d32744337d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F04%2F20131204184148_hhXUT.jpeg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572257755806&di=499d8ab1b99439f92ddc19d32744337d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F04%2F20131204184148_hhXUT.jpeg"));
    }
}
