/*
 * Copyright (C) 2021 pedroSG94.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pedro.rtpstreamer;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.pedro.rtpstreamer.customexample.RtmpActivity;
import com.pedro.rtpstreamer.utils.ActivityLink;
import com.pedro.rtpstreamer.utils.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView list;
    private List<ActivityLink> activities;

    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.transition.slide_in, R.transition.slide_out);
        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText(getString(R.string.version, BuildConfig.VERSION_NAME));

        list = findViewById(R.id.list);
        createList();
        setListAdapter(activities);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }

    private void createList() {
        activities = new ArrayList<>();
        ActivityLink vnetworkCloudflareHighPreset = new ActivityLink(
                new Intent(this, RtmpActivity.class),
                "VNetwork vs Cloudflare High Preset", JELLY_BEAN);
        vnetworkCloudflareHighPreset.setBitrate(8000);
        vnetworkCloudflareHighPreset.setWidth(1080);
        vnetworkCloudflareHighPreset.setHeight(1920);
        vnetworkCloudflareHighPreset.setUrl("rtmp://lms-public-ingest.swiftfederation.com/OM_Cloudflare_High/OM_Cloudflare_High?token=1644552828-cd25a29f6ee780c6b62883d65f8d8a5b");
        activities.add(vnetworkCloudflareHighPreset);

        ActivityLink vnetworkBestHighPreset = new ActivityLink(
                new Intent(this, RtmpActivity.class),
                "VNetwork vs Best High Preset", JELLY_BEAN);
        vnetworkBestHighPreset.setBitrate(2100);
        vnetworkBestHighPreset.setWidth(1080);
        vnetworkBestHighPreset.setHeight(1920);
        vnetworkBestHighPreset.setUrl("rtmp://lms-public-ingest.swiftfederation.com/OM_Vnetwork_High/OM_Vnetwork_High?token=1644552757-311107709fb6b6d47f51dc702271b09c");
        activities.add(vnetworkBestHighPreset);

        ActivityLink wowzaCloudflareHighPreset = new ActivityLink(
                new Intent(this, RtmpActivity.class),
                "Wowza vs Cloudflare High Preset", JELLY_BEAN);
        wowzaCloudflareHighPreset.setBitrate(8000);
        wowzaCloudflareHighPreset.setWidth(1080);
        wowzaCloudflareHighPreset.setHeight(1920);
        wowzaCloudflareHighPreset.setUrl("rtmp://ba32f0.entrypoint.cloud.wowza.com/app-1507j4P6/4500c947");
        activities.add(wowzaCloudflareHighPreset);

        ActivityLink wowzaBestHighPreset = new ActivityLink(
                new Intent(this, RtmpActivity.class),
                "Wowza vs Best High Preset", JELLY_BEAN);
        wowzaBestHighPreset.setBitrate(3400);
        wowzaBestHighPreset.setWidth(1080);
        wowzaBestHighPreset.setHeight(1920);
        wowzaBestHighPreset.setUrl("rtmp://a53b48.entrypoint.cloud.wowza.com/app-339C26d4/06219625");
        activities.add(wowzaBestHighPreset);

        ActivityLink vnetworkFastPreset = new ActivityLink(
                new Intent(this, RtmpActivity.class),
                "Vnetwork vs Fast Preset", JELLY_BEAN);
        vnetworkFastPreset.setBitrate(2200);
        vnetworkFastPreset.setWidth(608);
        vnetworkFastPreset.setHeight(1080);
        vnetworkFastPreset.setUrl("rtmp://lms-public-ingest.swiftfederation.com/OM_Vnetwork_Fast/OM_Vnetwork_Fast?token=1644719978-e581b95deeeb8f013edc708251c8e4f3");
        activities.add(vnetworkFastPreset);

        ActivityLink customStreamer = new ActivityLink(
                new Intent(this, RtmpActivity.class),
                "Custom\nStreamer", JELLY_BEAN);
        activities.add(customStreamer);
    }

    private void setListAdapter(List<ActivityLink> activities) {
        list.setAdapter(new ImageAdapter(activities));
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (hasPermissions(this, PERMISSIONS)) {
            ActivityLink link = activities.get(i);
            int minSdk = link.getMinSdk();
            if (Build.VERSION.SDK_INT >= minSdk) {
                startActivity(link.getIntent());
                overridePendingTransition(R.transition.slide_in, R.transition.slide_out);
            } else {
                showMinSdkError(minSdk);
            }
        } else {
            showPermissionsErrorAndRequest();
        }
    }

    private void showMinSdkError(int minSdk) {
        String named;
        switch (minSdk) {
            case JELLY_BEAN_MR2:
                named = "JELLY_BEAN_MR2";
                break;
            case LOLLIPOP:
                named = "LOLLIPOP";
                break;
            default:
                named = "JELLY_BEAN";
                break;
        }
        Toast.makeText(this, "You need min Android " + named + " (API " + minSdk + " )",
                Toast.LENGTH_SHORT).show();
    }

    private void showPermissionsErrorAndRequest() {
        Toast.makeText(this, "You need permissions before", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}