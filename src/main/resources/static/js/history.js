$(function () {
    var history = new Vue({
        el: '#history',
        data: {
            status_code: 2,
            history: []
        }
    });
    $('.weui-navbar__item').on('click', function () {
        $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass(
            'weui-bar__item_on');
    });
    $('#checking').click(function () {
        history.status_code = 2;
        get_history(2);
    });
    $('#passed').click(function () {
        history.status_code = 3;
        get_history(3);
    });
    $('#canceled').click(function () {
        history.status_code = 1;
        get_history(1);
    });
    $('#rejected').click(function () {
        history.status_code = 4;
        get_history(4);
    });
    $('#out_of_date').click(function () {
        history.status_code = 0;
        get_history(0);
    });

    function get_history(status_code) {
        $.ajax({
            url: location.href.split("meeting")[0] + "meeting/history/" + status_code,
            type: "GET",
            success: function (data) {
                if (data.errcode === 0) {
                    history.status_code = data.statusCode;
                    history.history = data.history;
                    nodialogs();
                } else {
                    confirm_dialog(data.errmsg);
                }
            },
            error: function () {
                confirm_dialog("获取记录失败");
            }
        });
        loading_toast("加载中");
    }
    //一开始载入审核中的记录
    get_history(2);
})