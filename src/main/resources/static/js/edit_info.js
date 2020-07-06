$(function () {
    loading_toast("数据加载中");
    var gender = 0;
    $.ajax({
        url: location.href.split("meeting")[0] + "meeting/profile",
        type: "GET",
        success: function (data) {
            show(data);
        }
    });

    //渲染数据
    function show(data) {
        if (data.errcode == 0) {
            $("#avatar").attr("src", data.avatar);
            $("#name").attr('placeholder', data.name)
            $("#userid").attr('placeholder', data.userid);
            if (data.role == 1) {
                $("#role").attr('placeholder', "学生");
            } else if (data.role == 2) {
                $("#role").attr('placeholder', "老师");
            }
            gender = data.gender;
            updateGender();
            if (data.age > 0) $("#age").val(data.age);
            $("#phone").val(data.phone);
            $("#email").val(data.email);
            nodialogs();
            $("#edit_page").fadeIn(100);
        } else {
            confirm_dialog(data.errmsg);
        }
    }

    //性别下拉框
    $('#gender').on('click', function () {
        weui.picker([{
            label: '未知',
            value: 0
        }, {
            label: '男',
            value: 1
        }, {
            label: '女',
            value: 2
        }], {
            onConfirm: function (result) {
                gender = result[0].value;
                updateGender();
            },
            title: '选择性别'
        });
    });
    //保存修改，提交数据
    $("#save").click(function () {
        $.ajax({
            url: location.href.split("meeting")[0] + "meeting/profile",
            type: "POST",
            data: {
                avatar: $("#avatar")[0].src,
                gender: gender,
                age: $("#age").val(),
                phone: $("#phone").val(),
                email: $("#email").val()
            },
            success: function (data) {
                show(data);
                if (data.errcode == 0) {
                    confirm_dialog("个人信息修改成功");
                }
            },
            error: function () {
                confirm_dialog("个人信息修改失败");
            }
        });
        clear();//清空所有个人信息框
        loading_toast("正在保存");
    });

    //清空个人信息框
    function clear() {
        $("#avatar").attr("src", '../images/defalt.png');
        $("#name").attr('placeholder', '姓名');
        $("#userid").attr('placeholder', '用户名');
        $("#role").attr('placeholder', "身份");
        $("#gender").val('未知');
        $("#age").val('');
        $("#phone").val('');
        $("#email").val('');
    }

    //改变性别
    function updateGender() {
        if (gender == 1) {
            $("#gender").val("男");
        } else if (gender == 2) {
            $("#gender").val("女");
        } else {
            $("#gender").val("未知");
        }
    }

    //上传头像
    $("#upload").change(function () {
        var files = $("#upload")[0].files;
        var formData = new FormData();
        if (files.length > 0) {
            for (var i = 0; i < files.length; i++) {
                formData.append("upload", files[i]);
            }
            $.ajax({
                type: 'POST',
                url: location.href.split("meeting")[0] + "meeting/profile/upload",
                data: formData,
                cache: false,
                processData: false,
                contentType: false,
                success: function (data) {
                    if (data.errcode == 0) {
                        $("#avatar").attr("src", data.errmsg);
                        complete_toast("上传成功");
                    } else {
                        confirm_dialog(data.errmsg);
                    }
                },
                error: function (request, errmsg) {
                    if (errmsg == 'timeout')
                        confirm_dialog("连接超时");
                    if (errmsg == 'error')
                        confirm_dialog("图片大小超出限制");
                }
            });
            loading_toast("上传中");
        }
    });
});