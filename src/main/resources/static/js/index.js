$(function () {
    var date;
    var room_selector = new Vue({
        el: '#room_selector',
        data: {
            list: [],
            selected: '0'
        }
    });
    var search_result = new Vue({
        el: '#search_result',
        data: {
            date: 0,
            rooms: []
        }
    });
    //下拉框
    $('#room_selector select').dropdown();
    ///日期插件
    $('#date').calendar({
        type: 'date',
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
    //请求全部会议室
    $.ajax({
        url: location.href.split("meeting")[0] + "meeting/rooms",
        type: "GET",
        success: function (data) {
            if (data.errcode === 0) {
                room_selector.list = data.rooms;
                nodialogs();
            } else {
                confirm_dialog(data.errmsg);
            }
        }
    });
    //发送筛选请求
    $('#search_rooms').click(function () {
            var roomId = room_selector.selected;
            var dateObject = $('#date').calendar('get date');
            var timeObject = $('#time').calendar('get date');
            //两者均返回时间戳，服务器拿到后再通过Date获取,-1代表未设置
            if (dateObject == null) {
                $('#date_tip').fadeIn(200);
                return;
            }
            if ($('#duration').val() == '') {
                $('#duration_tip').fadeIn(200);
                return;
            }
            var date = dateObject.getTime();
            search_result.date = date;
            var time = timeObject == null ? 0 : timeObject.getTime();
            var duration = $('#duration').val();
            var size = $('#size').val() === '' ? 0 : $('#size').val();
            $.ajax({
                url: location.href.split("meeting")[0] + "meeting/rooms",
                type: "POST",
                data: {
                    roomId: roomId,
                    date: date,
                    time: time,
                    duration: duration,
                    size: size
                },
                success: function (data) {
                    if (data.errcode === 0) {
                        search_result.rooms = data.rooms;
                        $('#result_tip').fadeIn(100);
                        $('#result_tip').transition('fade right in');
                        $('#search_result').transition('fade up in')
                    } else {
                        confirm_dialog(data.errmsg);
                    }
                }
            });
        }
    );
    //弹出必填提示
    $('#date_input').focus(function () {
        $('#date_tip').fadeOut(200);
    });
    $('#duration').focus(function () {
        $('#duration_tip').fadeOut(200);
    });
    //监听滚动条触顶触底
    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight + 100 > scrollHeight) {  //滚动到底部执行事件
            $('#tabbar').fadeOut(100);
        } else {
            $('#tabbar').fadeIn(100);
        }
        if (scrollTop === 0) {
            $('#tabbar').fadeIn(100);
        }
    });
});