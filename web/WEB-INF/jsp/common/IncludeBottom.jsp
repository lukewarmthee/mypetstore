<%--
  Created by IntelliJ IDEA.
  User: Lukewarmth
  Date: 2020年11月15日
  Time: 20点36分
--%>
</div>

<div id="Footer">
    <div id="PoweredBy">&nbsp<a href="www.csu.edu.cn">www.csu.edu.cn</a>
    </div>

    <!--列出用户喜欢的标题-->
    <div id="Banner">
        <c:if test="${sessionScope.account.bannerOption} != null">
         ${sessionScope.account.bannername}
        </c:if>
    </div>

</div>

</body>
</html>
