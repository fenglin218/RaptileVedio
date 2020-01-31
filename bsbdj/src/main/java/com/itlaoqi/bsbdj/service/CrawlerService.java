package com.itlaoqi.bsbdj.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itlaoqi.bsbdj.common.OgnlUtils;
import com.itlaoqi.bsbdj.entity.*;
import com.itlaoqi.bsbdj.mapper.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//处理核心业务逻辑
@Transactional //写在类上，默认所有方法均开启事务。懒人必备
public class CrawlerService {
    @Resource
    private SourceMapper sourceMapper;
    @Resource
    private ContentMapper contentMapper;
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ForumMapper forumMapper;
    Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    /**
     * 抓取最近的100条数据
     * @param context
     * @param template
     * @param np
     * @param channelId
     */
    @Transactional
    public void crawl(Map context , String template , String np , Integer channelId){

        String url = template.replace("{np}" , np);

        //String url = "http://c.api.budejie.com/topic/list/jingxuan/1/budejie-android-6.9.4/0-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00";
        //使用OKHttp的方法很简单，只需要遵守3步
        //1. 创建OKHttp对象
        OkHttpClient client = new OkHttpClient();
        //2.构建请求,设置要访问的url
        Request.Builder builder = new Request.Builder().url(url);
        //增加请求头,模拟Chrome浏览器
        builder.addHeader("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        //3. 发送请求
        //创建已设置好的请求对象
        Request  request= builder.build();
        String retText = null;
        for(int i = 0 ; i < 10 ; i++) {
            try {
                //发送请求,返回响应对象
                Response response = client.newCall(request).execute();
                //获取响应的数据
                retText = response.body().string();
                //抓取成功后跳出循环
                break;
            } catch (IOException e) {
                logger.warn("爬虫连接超时，正在准备第{}次重试，URL:{}", (i+1) ,url);
                continue;
            }
        }
        //10次抓取都失败，程序中断
        if(retText == null){
            logger.error("爬虫抓取失败，原因：连接超时,URL:" + url);
        }
        //转换为Map对象
        Gson gson = new Gson();
        Map result = gson.fromJson(retText , new TypeToken<Map>(){}.getType());
        Double dnp =   (Double)((Map)result.get("info")).get("np");
        //DecimalFormat代表对数字进行格式化，按要求显示 #号代表有这个数字则显示，没有则忽略 。0 ,代表没有的时候使用0占位
        //  123456.78        #########  -> 123456    #########.## ->123456.78   0000000000.000 -> 0000123456.780
        String strNP = new DecimalFormat("###########").format(dnp);

        //保存原始数据数据
        Source source = new Source();
        source.setChannelId(channelId);
        source.setCreateTime(new Date());
        source.setResponseText(retText);
        source.setState("WAITING");//等待被处理的状态
        source.setUrl(url);
        sourceMapper.insert(source);
        logger.info("数据抓取成功,NP:{},URL:{} " , strNP , url );



        int count = (int)context.get("count");
        count = count + 20;
        context.put("count" , count);
        //当累计抓取了100条数据的时候，即5次就终止爬虫
        if(count >= 100){
            return;
        }
        //只抓取最近的100条数据，每小时抓一次。大约百思每小时更新3~5条数据，所以抓最近的100条足够了
        //利用递归进行数据抓取
        crawl(context , template , strNP, channelId);
    }
    @Transactional
    public void cralwerRunner(){
        // URL模板
        String[] templates = new String[]{
                "http://c.api.budejie.com/topic/list/jingxuan/1/budejie-android-6.9.4/{np}-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
                ,"http://c.api.budejie.com/topic/list/jingxuan/41/budejie-android-6.9.4/{np}-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
                ,"http://c.api.budejie.com/topic/list/jingxuan/10/budejie-android-6.9.4/{np}-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
                ,"http://c.api.budejie.com/topic/list/jingxuan/29/budejie-android-6.9.4/{np}-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
                ,"http://s.budejie.com/topic/list/remen/1/budejie-android-6.9.4/{np}-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
        };
        for(int i = 0 ; i < templates.length ; i ++) {
            logger.info("正在抓取第{}个模块的数据" , (i+1));
            String template = templates[i];
            Map context = new HashMap();
            context.put("count", 0);//记录抓取的总数
            this.crawl(context, template, "0" , (i+1));
        }
    }

    /**
     * 对数据进行etl处理
     */
    //Transactional 事务
    //方法执行成功，自动事务提交， 如果遇到RuntimeException及其子类，则进行回滚
    @Transactional
    public void etl(){
        //查询所有等待处理的数据源
        List<Source> sources = sourceMapper.findByState("WAITING");
        for(Source source : sources){
            String json = source.getResponseText();
            //将字符串转换为Map对象
            Map<String,Object> root = new Gson().fromJson(json , new TypeToken<Map>(){}.getType());
            //获取每页20条数据的集合
            List<Map<String,Object>> list =  OgnlUtils.getListMap("list" , root);
            int i = 0 ;
            for(Map contentMap : list){
                i++;

                /*if(i == 4){
                    throw new RuntimeException("MMP,出错啦~");
                }*/
                //将每条内容保存到数据库
                createContent(source , contentMap);
            }
            //对处理完成的Source对象更新状态为Processed
            source.setState("PROCESSED");
            sourceMapper.updateByPrimaryKey(source);
        }
    }
    @Transactional
    public void createContent(Source source , Map contentMap){

        Long contentId = OgnlUtils.getNumber("id" , contentMap).longValue();
        //一个内容只允许插入一次，不允许重复
        if(contentMapper.selectByPrimaryKey(contentId) != null){
            logger.info("Content ID:{} ，内容已存在，此内容被忽略导入" , contentId  );
            return;
        }

        Content content = new Content();
        //频道编号
        content.setChannelId(source.getChannelId().longValue());
        //状态 4代表通过
        content.setStatus(OgnlUtils.getNumber("status" , contentMap).intValue());
        //评论总数
        content.setCommentCount(OgnlUtils.getNumber("comment" , contentMap).intValue());
        //收藏数
        content.setBookmarkCount(OgnlUtils.getNumber("bookmark", contentMap).intValue());
        //正文或标题
        content.setContentText(OgnlUtils.getString("text", contentMap));
        //点赞数量
        content.setLikeCount(OgnlUtils.getNumber("up", contentMap).intValue());
        //踩 数量
        content.setHateCount(OgnlUtils.getNumber("down", contentMap).intValue());
        //分享连接
        content.setShareUrl(OgnlUtils.getString("share_url", contentMap));
        //分享数量
        content.setShareCount(OgnlUtils.getNumber("forward", contentMap).intValue());
        //过审时间
        content.setPasstime(OgnlUtils.getString("passtime", contentMap));
        //内容类型
        content.setContentType(OgnlUtils.getString("type", contentMap));
        //内容编号
        content.setContentId(OgnlUtils.getNumber("id", contentMap).longValue());
        //数据源编号
        content.setSourceId(source.getSourceId());
        //创建时间
        content.setCreateTime(new Date());
        //保存content
        contentMapper.insert(content);
        if(content.getContentType().equals("video")){
            Video vid = new Video();
            //播放数量
            vid.setPlayfcount(OgnlUtils.getNumber("video.playfcount", contentMap).intValue() );
            //视频高
            vid.setHeight(OgnlUtils.getNumber("video.height", contentMap).intValue());
            //视频宽度
            vid.setWidth(OgnlUtils.getNumber("video.width", contentMap).intValue());
            //视频在线播放地址，默认以第一个为准
            List<String> videoUrl = OgnlUtils.getListString("video.video", contentMap);
            vid.setVideoUrl(videoUrl.size() > 0 ? videoUrl.get(0) : null);
            List<String> downloadUrl = OgnlUtils.getListString("video.download", contentMap);
            //视频下载播放地址，默认以第一个为准
            vid.setDownloadUrl(downloadUrl.size() > 0 ? downloadUrl.get(0) : null);
            vid.setDuration(OgnlUtils.getNumber("video.duration", contentMap).intValue());
            //播放数量
            vid.setPlaycount(OgnlUtils.getNumber("video.playcount", contentMap).intValue());
            //视频缩略图地址，默认以第一个为准
            List<String> thumb = OgnlUtils.getListString("video.thumbnail", contentMap);
            vid.setThumb(thumb.size() > 0 ? thumb.get(0) : null);
            //视频小缩略图地址，默认以第一个为准
            List<String> thumbSmall = OgnlUtils.getListString("video.thumbnail_small", contentMap);
            vid.setThumbSmall(thumbSmall.size() > 0 ? thumbSmall.get(0) : null);
            //内容编号
            vid.setContentId(content.getContentId());
            videoMapper.insert(vid);

        }else if(content.getContentType().equals("image")){
            Image image = new Image();
            //原始高度
            image.setRawHeight(OgnlUtils.getNumber("image.height", contentMap).intValue());
            //原始宽度
            image.setRawWidth(OgnlUtils.getNumber("image.width", contentMap).intValue());
            //下载地址，第一个为准
            List<String> downloadUrl = OgnlUtils.getListString("image.download_url", contentMap);
            image.setWatermarkerUrl(downloadUrl.size() > 0 ? downloadUrl.get(0) : null);
            //缩略图地址，第一个为准
            List<String> thumbSmall = OgnlUtils.getListString("image.thumbnail_small", contentMap);
            image.setThumbUrl(thumbSmall.size() > 0 ? thumbSmall.get(0) : null);
            //大图地址，第一个为准
            List<String> bigUrl = OgnlUtils.getListString("image.big", contentMap);
            image.setBigUrl(bigUrl.size() > 0 ? bigUrl.get(0) : null);
            //内容编号
            image.setContentId(content.getContentId());
            imageMapper.insert(image);

        }else if(content.getContentType().equals("gif")){
            Image gif = new Image();
            List<String> bigUrl = OgnlUtils.getListString("gif.images", contentMap);
            gif.setBigUrl(bigUrl.size() > 0 ? bigUrl.get(0) : null);
            gif.setRawHeight(OgnlUtils.getNumber("gif.height", contentMap).intValue());
            gif.setRawWidth(OgnlUtils.getNumber("gif.width", contentMap).intValue());
            List<String> downloadUrl = OgnlUtils.getListString("gif.download_url", contentMap);
            gif.setWatermarkerUrl(downloadUrl.size() > 0 ? downloadUrl.get(0) : null);
            List<String> thumb = OgnlUtils.getListString("gif.thumbnail_small", contentMap);
            gif.setThumbUrl(thumb.size() > 0 ? thumb.get(0) : null);
            gif.setContentId(content.getContentId());
            imageMapper.insert(gif);
        }

        //配置用户，用户无则创建，无则加载
        Number nuid = OgnlUtils.getNumber("u.uid" , contentMap);
        //小概率的情况下原始数据中是没有这个uid的
        if(nuid != null){
            //查询用户是否存在
            User user  = userMapper.selectByPrimaryKey(nuid.longValue());
            //不存在则创建
            if(user == null){
                user = new User();
                //获取第一个头像
                List<String> header = OgnlUtils.getListString("u.header", contentMap);
                user.setHeader(header.size() > 0 ? header.get(0) : null);
                //用户编号
                user.setUid(OgnlUtils.getNumber("u.uid", contentMap).longValue());
                //是否vip 1 是 0 不是
                user.setIsVip(OgnlUtils.getBoolean("u.is_vip", contentMap) ? 1 : 0);
                //是否V
                user.setIsV(OgnlUtils.getBoolean("u.is_v", contentMap) ? 1 : 0);
                //房间url
                user.setRoomUrl(OgnlUtils.getString("u.room_url", contentMap));
                //房间名
                user.setRoomName(OgnlUtils.getString("u.room_name", contentMap));
                //房间角色
                user.setRoomRole(OgnlUtils.getString("u.room_role", contentMap));
                //房间图标
                user.setRoomIcon(OgnlUtils.getString("u.room_icon", contentMap));
                //昵称
                user.setNickname(OgnlUtils.getString("u.name", contentMap));
                userMapper.insert(user);
            }
            //更新userid
            content.setUid(user.getUid());
            contentMapper.updateByPrimaryKey(content);
        }
        //插入评论数据
        List<Map<String,Object>> comments = OgnlUtils.getListMap("top_comments" , contentMap);
        if(comments != null){
            //遍历评论
            for(Map commentMap : comments){
                Comment comment = new Comment();
                //评论对象
                comment.setCommentId(OgnlUtils.getNumber("id" , commentMap).longValue());
                //评论内容
                comment.setCommentText(OgnlUtils.getString("content", commentMap));
                //过审时间
                comment.setPasstime(OgnlUtils.getString("passtime", commentMap));
                //配置用户,有则加载,无则创建
                Long commentUid = OgnlUtils.getNumber("u.uid", commentMap).longValue();
                //评论用户
                User user = userMapper.selectByPrimaryKey(commentUid);
                //有则加载，无则创建
                if (user == null) {
                    user = new User();
                    List<String> header = OgnlUtils.getListString("u.header", commentMap);
                    user.setHeader(header.size() > 0 ? header.get(0) : null);
                    user.setUid(OgnlUtils.getNumber("u.uid", commentMap).longValue());
                    user.setIsVip(OgnlUtils.getBoolean("u.is_vip", commentMap) ? 1 : 0);
                    user.setRoomUrl(OgnlUtils.getString("u.room_url", commentMap));
                    user.setRoomName(OgnlUtils.getString("u.room_name", commentMap));
                    user.setRoomRole(OgnlUtils.getString("u.room_role", commentMap));
                    user.setRoomIcon(OgnlUtils.getString("u.room_icon", commentMap));
                    user.setNickname(OgnlUtils.getString("u.name", commentMap));
                    userMapper.insert(user);
                }
                //设置用户编号
                comment.setUid(user.getUid());
                //设置内容编号
                comment.setContentId(content.getContentId());
                //插入数据
                commentMapper.insert(comment);

            }
        }

        //更新圈子
        List<Map<String, Object>> tags = OgnlUtils.getListMap("tags", contentMap);
        //只获取第一个论坛
        if (tags.size() > 0) {
            Map<String, Object> tag = tags.get(0);
            Long forumId = OgnlUtils.getNumber("id", tags.get(0)).longValue();
            Forum forum = forumMapper.selectByPrimaryKey(forumId);
            //查找论坛,有则加载,无则创建
            if (forum == null) {
                forum = new Forum();
                //帖子数量
                forum.setPostCount(OgnlUtils.getNumber("post_number", tag).intValue());
                //logo地址
                forum.setLogo(OgnlUtils.getString("image_list", tag));
                //排序
                forum.setForumSort(OgnlUtils.getNumber("forum_sort", tag).intValue());
                //论坛状态
                forum.setForumStatus(OgnlUtils.getNumber("forum_status", tag).intValue());
                //论坛编号
                forum.setForumId(forumId);
                //论坛信息
                forum.setInfo(OgnlUtils.getString("info", tag));
                //论坛名称
                forum.setName(OgnlUtils.getString("name", tag));
                //用户总量
                forum.setUserCount(OgnlUtils.getNumber("sub_number", tag).intValue());
                //保存数据
                forumMapper.insert(forum);
            }
            //更新内容圈子编号
            content.setForumId(forum.getForumId());
            contentMapper.updateByPrimaryKey(content);
        }
        logger.info("Content ID:{} ，内容成功导入" , contentId  );


    }

    //查询方法@Transactional(Propagation=NOT_SUPPORT , readyonly=true)


    /**
     *
     * @param page 你要查询第几页
     * @param rows 每页多少行
     * @return
     */
    public Page<Map> findContents(Integer page , Integer rows , Integer channelId , String contentType , String keyword){
        //启用分页
        //PageHelper中自动帮我们生成limit 分页语句
        //以及自动帮我们执行查询总数的countSQL

        Map params = new HashMap();
        if(channelId != null && channelId != -1){
            params.put("channelId" , channelId);
        }

        if(contentType != null && !contentType.equals("-1")){
            params.put("contentType" , contentType);
        }
        if(keyword != null && !keyword.trim().equals("")){
            params.put("keyword" , "%" + keyword + "%");
        }

        PageHelper.startPage(page , rows);
        Page<Map> list = (Page)contentMapper.findByParams(params);
        return list;
    }

    public void delete(Long contentId){
        contentMapper.deleteByPrimaryKey(contentId);
    }

    public Map getPreviewData(Long contentId){
        Content content = contentMapper.selectByPrimaryKey(contentId);
        User user = userMapper.selectByPrimaryKey(content.getUid());
        //有的content是没有Forum对象的
        Forum forum = null;
        if(content.getForumId() != null){
            forum = forumMapper.selectByPrimaryKey(content.getForumId());
        }
        List<Map> comments = commentMapper.findByContentId(contentId);
        Map previewData = new HashMap();
        previewData.put("content" , content);
        previewData.put("comments" , comments);
        previewData.put("forum" , forum);
        previewData.put("user" , user);
        if(content.getContentType().equals("video")){
            previewData.put("video" , videoMapper.findByContentId(contentId).get(0));
        }else if(content.getContentType().equals("gif") || content.getContentType().equals("image")){
            previewData.put("image" , imageMapper.findByContentId(contentId).get(0));
        }
        return previewData;
    }
}
