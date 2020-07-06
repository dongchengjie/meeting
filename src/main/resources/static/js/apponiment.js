$(function () {
    ///日期插件
    $('#datetime').calendar({
        type: 'datetime',
        ampm: false,
        today: true,
        text: {
            days: ['日', '一', '二', '三', '四', '五', '六'],
            months: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            monthsShort: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二'],
            today: '今天',
            now: '现在',
            am: '上午',
            pm: '下午'
        }
    });
    $('#time').calendar({
        type: 'time',
        ampm: false
    });
    $('#appoint').click(function () {
        //非空判断
        var roomId = $('#place_input').attr('title');
        var dateObject = $('#datetime').calendar('get date');
        //两者均返回时间戳，服务器拿到后再通过Date获取,-1代表未设置
        if (dateObject == null) {
            $('#datetime_tip').fadeIn(200);
            return;
        }
        if ($('#duration_input').val() == '') {
            $('#duration_tip').fadeIn(200);
            return;
        }
        if ($('#size_input').val() == '') {
            $('#size_tip').fadeIn(200);
            return;
        }
        if ($('#phone_input').val() == '') {
            $('#phone_tip').fadeIn(200);
            return;
        }
        if ($('#purpose_input').val() == '') {
            $('#purpose_tip').fadeIn(200);
            return;
        }
        var datetime = dateObject.getTime();
        var duration = $('#duration_input').val();
        var size = $('#size_input').val();
        var phone = $('#phone_input').val();
        var purpose = $('#purpose_input').val();
        var note = $('#note_input').val();
        $.ajax({
            url: location.href.split("meeting")[0] + "meeting/appointment",
            type: "POST",
            data: {
                roomId: roomId,
                datetime: datetime,
                duration: duration,
                size: size,
                phone: phone,
                purpose: purpose,
                note: note
            },
            success: function (data) {
                if (data.errcode === 0) {
                    yesorno_dialog("提示", "预约申请提交成功");
                } else {
                    confirm_dialog(data.errmsg);
                }
            }
        });
    })
    //弹出必填提示
    $('#room_selector').click(function () {
        $('#room_tip').fadeOut(200);
    });
    $('#datetime_input').focus(function () {
        $('#datetime_tip').fadeOut(200);
    });
    $('#duration_input').focus(function () {
        $('#duration_tip').fadeOut(200);
    });
    $('#size_input').focus(function () {
        $('#size_tip').fadeOut(200);
    });
    $('#phone_input').focus(function () {
        $('#phone_tip').fadeOut(200);
    });
    $('#purpose_input').focus(function () {
        $('#purpose_tip').fadeOut(200);
    });
});