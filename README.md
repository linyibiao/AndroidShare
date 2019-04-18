# AndroidShare
Android特色控件的插件化

network的使用：

    compile 'com.lyb.besttimer:network:0.0.7'
对应jcenter链接<https://bintray.com/linyibiao/maven/plugin_network/0.0.7>

pluginwidget的使用：

    compile 'com.lyb.besttimer:pluginwidget:0.0.134'
对应jcenter链接<https://bintray.com/linyibiao/maven/plugin_widget/0.0.134>

rxandroid的使用：

    compile 'com.lyb.besttimer:rxandroid:0.0.6'
对应jcenter链接<https://bintray.com/linyibiao/maven/rxandroid/0.0.6>

cameracore的使用：

    compile 'com.lyb.besttimer:cameracore:0.0.3'
对应jcenter链接<https://bintray.com/linyibiao/maven/plugin_camera/0.0.3>

------------------------------------------------------------------------------------------------------

cameracore

集成camera1和camera2

示范路径：相机-->>>相机

接入：

    compile 'com.lyb.besttimer:cameracore:0.0.3'
    compile 'com.lyb.besttimer:pluginwidget:0.0.134'
    compile 'com.github.tbruyelle:rxpermissions:0.10.2'

启动：

    startActivityForResult(new Intent(this, CameraMixActivity.class), cameraMixCode);

接收：

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case cameraMixCode:
                    String fileUrl = data.getStringExtra(CameraConstants.fileUrl);//资源本地链接
                    CameraResultCaller.ResultType resultType = (CameraResultCaller.ResultType) data.getSerializableExtra(CameraConstants.resultType);
                    if (resultType == CameraResultCaller.ResultType.PICTURE) {
                        // TODO: 2019/4/18 图片处理
                    } else if (resultType == CameraResultCaller.ResultType.VIDEO) {
                        // TODO: 2019/4/18 视频处理
                    }
                    break;
            }
        }
    }
