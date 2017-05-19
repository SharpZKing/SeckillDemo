/* 存放主要交互逻辑JS 模块化*/
var seckill = {
    /* 封装秒杀相关ajax的URL*/
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/'+seckillId+"/exposer";
        },
        execution: function (seckillId, md5) {
            return '/seckill/'+seckillId+"/"+md5+"/execution";
        }
    },

    validatePhone: function (phone) {
      if (phone && phone.length==11 && !isNaN(phone)) {
          return true;
      }else{
          return false;
      }
    },

    handleSeckill:function (seckillId,node) {
        /*时间完成后回调时间，获取秒杀地址*/
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            //在回调函数中执行交互逻辑
            if (result && result['success']){
                var exposer = result['data'];
                if (exposer['exposed']){
                    //开启秒杀
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    $('#killBtn').one('click',function () {
                        $(this).addClass('disabled');
                        $.post(killUrl, {}, function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }
                        });
                    });
                    node.show();
                }else{
                    //未开始秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];

                    seckill.countdown(seckillId,now,start,end);
                }
            }else{
                console.log('result='+result);
            }
        });
    },

    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box')
        if (nowTime > endTime){
            seckillBox.html("秒杀结束");
        }else if (nowTime < startTime){
            //seckillBox.html('秒杀未开始');
            var killTime = new Date(startTime + 1000);

            seckillBox.countdown(killTime,function (event) {
               var format =  event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
               seckillBox.html(format);
            }).on('finish.countdown', function () {
                /*时间完成后回调时间，获取秒杀地址*/
                seckill.handleSeckill(seckillId, seckillBox);
            });

        }else{
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },

    /*详情页秒杀逻辑*/
    detail: {
      /* 详情页初始化*/
      init: function (params) {
          //手机验证、计时
          var killPhone = $.cookie('killPhone');


          if (!seckill.validatePhone(killPhone)) {
              //绑定phone
              var killPhoneModal = $('#killPhoneModal');
              killPhoneModal.modal({
                  show: true,
                  backdrop: 'static',//禁止位置关闭
                  keyboard: false
              });
              $('#killPhoneBtn').click(function () {
                  var inputPhone = $('#killPhoneKey').val();
                  if (seckill.validatePhone(inputPhone)) {
                      //刷新页面 //写入cookie
                      $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});

                      window.location.reload();
                  } else {
                      $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show();

                  }

              });
          }

          var startTime = params['startTime'];
          var endTime = params['endTime'];
          var seckillId = params['seckillId'];

          $.get(seckill.URL.now(), {}, function () {
            if (result && result['success']){
                var nowTime = result['data'];
                seckill.countdown(seckillId, nowTime, startTime, endTime);
            }else{

            }
          });
      }
    }
}