$(function () {
    var date = $('#date').html();
    var roomId = $('#roomId').html();
    $('#appoint').attr('href', '/meeting/appointment/' + roomId);
    $('.menu .item')
        .tab();
    var that_periods = new Vue({
        el: '#that_periods',
        data: {
            that: []
        }
    });
    var all_periods = new Vue({
        el: '#all_periods',
        data: {
            all: []
        }
    });
    that(); //加载完显示当日预约
    $('#that').click(function () {
        that();
    });

    function that() {
        $.ajax({
            url: location.href.split("meeting")[0] + "meeting/room/periods",
            type: "GET",
            data: {
                roomId: roomId,
                type: "that",
                date: date
            },
            success: function (data) {
                if (data.errcode === 0) {
                    that_periods.that = data.periods;
                    if (that_periods.that.length > 0) {
                        $('.empty_tip').hide();
                        $('#that_table').fadeIn(200);
                    } else {
                        $('.empty_tip').show();
                        $('#that_table').fadeOut(200);
                    }
                } else {
                    confirm_dialog(data.errmsg);
                }
            }
        });
    }

    $('#all').click(function () {
        $.ajax({
            url: location.href.split("meeting")[0] + "meeting/room/periods",
            type: "GET",
            data: {
                roomId: roomId,
                type: "all",
                date: date
            },
            success: function (data) {
                if (data.errcode === 0) {
                    all_periods.all = data.periods;
                    if (all_periods.all.length > 0) {
                        $('.empty_tip').hide();
                        $('#all_table').fadeIn(200);
                    } else {
                        $('.empty_tip').show();
                        $('#all_table').fadeOut(200);
                    }
                } else {
                    confirm_dialog(data.errmsg);
                }
            }
        });
    });
});