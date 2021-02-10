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
                    window.open("https://github.com/login/oauth/authorize?client_id=ac4b3764221afc62cff6&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
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
        //自动插入json数据
        $.getJSON( "/comment/"+id, function( data ) {
            var subCommentContainer= $("#comment-"+id);
            $.each(data.data,function(index,comment){
                var c=$("<div/>",{
                    "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                        html:comment.content
                });
                //被选元素的开头（仍位于内部）插入指定内容。
                subCommentContainer.prepend(c);
            });

            //展开二级评论
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        });
    }
}

