$(function () {
    //点击弹窗按钮后，弹窗消失
    $('#dialogs').on('click', '.weui-dialog__btn', function () {
        $(this).parents('.js_dialog').fadeOut(200);
    });
});

//加载中
function loading_toast(loading_msg) {
    //关闭其他对话框
    $('#confirm').fadeOut(100);
    $('#complete').fadeOut(100);
    $('#yesorno').fadeOut(100);
    //设置信息
    $('#loading_msg').html(loading_msg);
    $('#loading').fadeIn(200);
}

//已完成
function complete_toast(complete_msg) {
    //关闭其他对话框
    $('#confirm').fadeOut(100);
    $('#loading').fadeOut(100);
    $('#yesorno').fadeOut(100);
    //设置信息
    $('#complete_msg').html(complete_msg);
    $('#complete').fadeIn(200);
    setTimeout(function () {
        $('#complete').fadeOut(100);
    }, 1500);
}

function confirm_dialog(confirm_msg) {
    //关闭其他对话框
    $('#loading').fadeOut(100);
    $('#complete').fadeOut(100);
    $('#yesorno').fadeOut(100);
    //设置信息
    $('#confirm_msg').html(confirm_msg);
    $('#confirm').fadeIn(200);
}

function yesorno_dialog(yesorno_title, yesorno_msg) {
    //关闭其他对话框
    $('#loading').fadeOut(100);
    $('#complete').fadeOut(100);
    $('#confirm').fadeOut(100);
    //设置信息
    $('#yesorno_title').html(yesorno_title);
    $('#yesorno_msg').html(yesorno_msg);
    $('#yesorno').fadeIn(200);
}

function nodialogs() {
    $('#loading').fadeOut(100);
    $('#complete').fadeOut(100);
    $('#confirm').fadeOut(100);
    $('#yesorno').fadeOut(100);
}