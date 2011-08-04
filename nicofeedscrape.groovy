import org.cyberneko.html.parsers.SAXParser
 
def rankUrl = 'http://www.nicovideo.jp/ranking'
def kinds = ['総合':'fav', 'マイリスト':'mylist', '再生':'view', 'コメント':'res']
def spans = ['毎時':'hourly', 'デイリー':'daily', '週間':'weekly', '月間':'monthly', '合計':'total']
 
def rankHtml = new XmlSlurper(new SAXParser()).parse(rankUrl)
 
def groups = rankHtml.'BODY'.'DIV'.'DIV'.'DIV'.'TABLE'.'TBODY'.'TR'.'TD'.'TABLE'.'TBODY'.'TR'.'TH'.'A'
def groupMap = [:]
groups.each{groupMap[it.text()] = it.@href.toString().split('/')[6]}
// groupMap.each{println 'GROUP: '+it.key + ', ' + it.value}
 
 
println '<HTML><BODY>'
 
for(kind in kinds){
    for(span in spans){
        println '<A HREF="' + rankUrl + '/' + kind.value + '/' + span.value + '/' + 'all' + '?rss=2.0">'
        println '' + 'カテゴリ合算' + ' - ' + kind.key + ' - ' + span.key + '</A><BR>'
    }
}
 
 
for(group in groupMap){
    def groupUrl = rankUrl + '/fav/daily/' + group.value
    // println 'GROUP URL: ' + groupUrl
    def groupHtml = new XmlSlurper(new SAXParser()).parse(groupUrl)
    def categories = groupHtml.'BODY'.'DIV'.'DIV'.'DIV'.'DIV'.'DIV'.'DIV'.'TABLE'.'TBODY'.'TR'.'TD'.'FORM'.'SELECT'.'OPTION'
    def categoryMap = [:]
    // categories.each{println 'CATEGORY: ' + it.text() + ', ' + it.@value.toString()}
    categories.each{categoryMap[it.text()] = it.@value.toString().split('/')[6]}
 
 
    println '<P>'
    println '<H3>カテゴリグループ: ' + group.key + '</H3><BR>'
    for(category in categoryMap){
        for(kind in kinds){
            for(span in spans){
                println '<A HREF="' + rankUrl + '/' + kind.value + '/' + span.value + '/' + category.value + '?rss=2.0">'
                println '' + category.key + ' - ' + kind.key + ' - ' + span.key + '</A><BR>'
            }
        }
    }
    println '</P>'
}
println '</BODY></HTML>'
