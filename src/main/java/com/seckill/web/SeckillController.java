package com.seckill.web;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.Seckill;
import com.seckill.enums.SeckillState;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by zjfsharp on 2017/5/17.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        List<Seckill> lists = seckillService.getSeckillList() ;
        System.out.println(lists.get(0).getName());
        model.addAttribute("lists",lists);

        //list.jsp + model = ModelAndView
        return "list"; //WEB-INF/jsp/list.jsp

    }


    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if (seckillId == null){
            return "redirect:/seckill/list";
        }

        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null){
            return "forward:/seckill/list";
        }

        model.addAttribute("seckill",seckill);
        return "detail";

    }

    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody //json
    public SeckillResult<Exposer> exposer(Long seckillId){

        SeckillResult<Exposer> result;

        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillResult<Exposer>(false,e.getMessage());
        }

        return result;

        /*if (seckillId == null){
            return new SeckillResult(false, "请求参数错误");
        }

        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer == null){
            return new SeckillResult<Exposer>(false,"秒杀未开始");
        }

        return new SeckillResult<Exposer>(true, exposer);*/

    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody //json
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5,
                                                   @CookieValue(value = "phone",required = false)Long phone){
        //springmvc valid TODO
        if (phone == null){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        SeckillResult<SeckillExecution> result;

        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId,phone,md5);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }catch (RepeatKillException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillState.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false,seckillExecution);
        }catch (SeckillCloseException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillState.ERROR);
            return new SeckillResult<SeckillExecution>(false,seckillExecution);
        }catch (Exception e){
            //logger.error(e.getMessage(),e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillState.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false,e.getMessage());
        }

        return result;
    }

    @RequestMapping(name = "/time/now", method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody //json
    public SeckillResult<Long> time(){
        Date date = new Date();
        return new SeckillResult<Long>(true,date.getTime());
    }



}
