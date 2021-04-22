


class FriendCircleViewModel {
  /// 用户名
  final String userName;

  /// 用户头像地址
  final String userImgUrl;

  /// 说说
  final String saying;

  /// 图片
  final String pic;

  ///发布时间
  final String publishTime;

  /// 评论内容
  final List<Comment> comments;

  const FriendCircleViewModel({
    this.userName,
    this.userImgUrl,
    this.saying,
    this.pic,
    this.publishTime,
    this.comments,
  });
}


class Comment {
  /// 是否发起人
  final bool source;

  /// 评论者
  final String fromUser;

  /// 被评论者
  final String toUser;

  // 评论内容
  final String content;

  const Comment({
    this.source,
    this.fromUser,
    this.toUser,
    this.content,
  });
}
