//执行post请求
function comment2target(targetId,type,content){
    if (!content) {
        alert("不能回复空内容");//前端弹窗
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
                // $("#comment_section").hide();
            }
            if (response.code == 2003) {
                var isAccepted = confirm(response.message)//弹窗
                //isAccepted对应弹窗中的确定与取消
                //确定
                if (isAccepted) {
                    //登录地址
                    window.open("https://github.com/login/oauth/authorize?client_id=ac4b3764221afc62cff6&redirect_uri=http://120.25.171.3/callback&scope=user&state=1")
                    //确认登录时存一个key value
                    //存入浏览器中
                    window.localStorage.setItem("closable", true);
                }
            } else {
                // alert(response.message)
            }
            console.log(response);
        },
        dataType: "json",
    });
}

//提交一级回复
function post() {
    //围棋项目中也是通隐藏的方式来在js中获取来自后端注入的值
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    //一级评论
    comment2target(questionId,1,content);
}

//提交二级回复
function comment(e){
    //获取页面中的评论回复内容
    var commentId=e.getAttribute("data-id");
    var content=$("#input-"+commentId).val();
    //发送页面数据，请求服务器数据（DTO）
    comment2target(commentId,2,content)
}

//展开二级评论
function collapseComments(e) {
    //获取对应元素的属性
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    //获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer= $("#comment-"+id);
        //防止二级评论重复
        if(subCommentContainer.children().length!=1){
            //展开二级评论
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        }else{
            //自动插入json数据
            $.getJSON( "/comment/"+id, function( data ) {
                $.each(data.data.reverse(),function(index,comment){

                    //拼接插入内容
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    //被选元素的开头（仍位于内部）插入指定内容。
                    //插入二级评论
                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                //标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    //获取已经存在的标签
    var previous = $("#tag").val();
    //判断已经存在的标签是否包含value
    //indexof()如果要检索的字符串值没有出现，则该方法返回 -1
    if (previous.indexOf(value) == -1){
        if (previous) {
            $("#tag").val(previous + ',' + value);
        }else {
            $("#tag").val(value);
        }
    }

}

function showSelectTag() {
    $("#select-tag").show();


}
function deleteQuestion(e) {
    var questionId = e.getAttribute("data-id");
    console.log(questionId);
    var isDeleted = confirm("delete forever ?");
    if (isDeleted) {
        window.location.replace("/profile/questions/delete/"+questionId);
    }
}


function thumbComments(e) {
    debugger;
    var thumbId = e.getAttribute("id");
    var url = e.getAttribute("data-id");
    var tags =$("#"+thumbId).children("#thumbChildElement");

    $.getJSON("/thumb/" + url ,function(data) {
        //span标签赋值用html,表单一般用val
        tags.html(data);
    });


}

function sendActiveEmail() {
    var email= $('input[name="email"]').val();
    if (email == null || email.trim().length === 0) {
        alert("请输入邮箱！");
        return;
    }
    $.getJSON("/sendActiveEmail/" + email,function (data) {
        if (data.code === 200) {
            invokeSetTime("#send-email-btn");
        } else {
            alert(data.message);
        }
    });

}

function sendModifyEmail() {
    var email= $('input[name="email"]').val();
    if (email == null || email.trim().length === 0) {
        alert("请输入邮箱！");
        return;
    }
    $.getJSON("/sendModifyEmail/" + email,function (data) {
        if (data.code === 200) {
            invokeSetTime("#send-email-btn");
        } else {
            alert(data.message);
        }
    });

}


function invokeSetTime(obj) {
    let countdown = 60;
    setTime(obj);

    function setTime(obj) {
        if (countdown === 0) {
            $(obj).attr("disabled", false);
            $(obj).text("GetCode");
            countdown = 60;
            return;
        } else {
            $(obj).attr("disabled", true);
            $(obj).text("(" + countdown + ") s resend");
            countdown--;
        }
        setTimeout(function () {
            setTime(obj)
        }, 1000);
    }
}
function modify() {
    window.location.replace("/modify");
}
function isImage() {
    debugger
    let filePath = $('input[name="head_portrait"]').val();
    let fileFormat = filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
    if (!fileFormat.match(/.png|.jpg|.jpeg/)) {
        alert("Please upload image");
        return false;
    }else {
        return true;
    }

}

function star(e) {
    let starId = e.getAttribute("id");
    let starSpan = $("h4.question-AliLogo span.glyphicon-star");
    starSpan.addClass("active");
    $.ajax({
        type: "post",
        url: "/star",
        data: starId,
        dataType: "application/json",
        success: function (data) {
            if (data.code == 200) {
                alert(data.code);
            }
        },
        error: function (data) {
            alert("Already Stared")
        }
    });

}
function unstar(e) {
    let id = e.getAttribute("data-id");
    $.getJSON("/unstar/" + id,function (data) {
        if (data.code === 200) {
            alert("success");
            window.location.reload();
        } else {
            alert(data.message);
        }
    });

}

