$(function () {
    $('#my_page').fadeOut();
    loading_toast("数据加载中");
    $.ajax({
        url: location.href.split("meeting")[0] + "meeting/profile",
        type: "GET",
        success: function (data) {
            if (data.errcode == 0) {
                $("#avatar").attr("src", data.avatar);
                $("#name").html(data.name);
                $("#userid").html(data.userid);
                $('#my_page').fadeIn();
                nodialogs();
            } else {
                confirm_dialog(data.errmsg);
            }
        }
    });
});