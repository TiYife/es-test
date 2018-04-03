(function ($) {
    $.extend({
        "signCalendar":function (options) {
            var opts=$.extend(defaults,options);


                var dateArray = opts.signedDate; // 已经签到的

                var $dateBox = $("#js-qiandao-list"),
                    $currentDate = $(".current-date"),
                    $qiandaoBnt = $("#js-just-qiandao"),
                    _html = '',
                    _handle = true,
                    myDate = new Date();
                var currentMouth=(opts.currentMonth>0?opts.currentMonth:parseInt(myDate.getMonth() + 1));
                $currentDate.text(myDate.getFullYear() + '年' + parseInt(myDate.getMonth() + 1) + '月' + myDate.getDate() + '日');

                var monthFirst = new Date(myDate.getFullYear(),currentMouth-1, 1).getDay();

                var d = new Date(myDate.getFullYear(),currentMouth, 0);
                var totalDay = d.getDate(); //获取当前月的天数

                for (var i = 0; i < 42; i++) {
                    _html += ' <li><div class="qiandao-icon"></div></li>'
                }
                $dateBox.html(_html) //生成日历网格

                var $dateLi = $dateBox.find("li");
                for (var i = 0; i < totalDay; i++) {
                    $dateLi.eq(i + monthFirst).addClass("date" + parseInt(i + 1));
                    for (var j = 0; j < dateArray.length; j++) {
                            if(parseInt( dateArray[j].substr(0,2))==currentMouth&&(parseInt( dateArray[j].substr(2,2))-1==i) )
                                $dateLi.eq(i + monthFirst).addClass("qiandao");
                    }
                } //生成当月的日历且含已签到

                $(".date" + myDate.getDate()).addClass('able-qiandao');

                $dateBox.on("click", "li", function() {
                    if ($(this).hasClass('able-qiandao') && _handle) {
                        $(this).addClass('qiandao');
                        qiandaoFun();
                    }
                }) //签到

                $qiandaoBnt.on("click", function() {
                    if (_handle) {
                        qiandaoFun();
                    }
                }); //签到

                function qiandaoFun() {
                    $qiandaoBnt.addClass('actived');
                    openLayer("qiandao-active", qianDao);
                    _handle = false;
                }

                function qianDao() {
                    $(".date" + myDate.getDate()).addClass('qiandao');
                }

            function openLayer(a, Fun) {
                $('.' + a).fadeIn(Fun)
            } //打开弹窗

            var closeLayer = function() {
                $("body").on("click", ".close-qiandao-layer", function() {
                    $(this).parents(".qiandao-layer").fadeOut()
                })
            }() //关闭弹窗

            $("#js-qiandao-history").on("click", function() {
                openLayer("qiandao-history-layer", myFun);

                function myFun() {
                    console.log(1)
                } //打开弹窗返回函数
            })

        }
    });
    var defaults={
        signedDate:["0701"],
        currentMonth:-1
    };
})(window.jQuery);
