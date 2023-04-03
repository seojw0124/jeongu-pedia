package com.example.jeongu_pedia.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jeongu_pedia.R
import com.example.jeongu_pedia.ReviewActivity
import com.example.jeongu_pedia.databinding.FragmentHomeBinding
import com.example.jeongu_pedia.datamodel.Movie
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(R.layout.fragment_home) {
    private var binding: FragmentHomeBinding? = null
    private lateinit var homeAdapter: HomeAdapter

    private val database = Firebase.firestore
    private var movieColl = database.collection("movies")

    private var movieList = mutableListOf<Movie>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        movieList.clear()

        movieColl.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result
                if (!document.isEmpty) {
                    val users = document.toObjects(Movie::class.java) // 역직렬화 동작. 클래스이 멤버변수가 초기화가 되어 있어야 함.
                    users.forEach {
                        var movie = Movie(it.id, it.title, it.poster, it.pubDate, it.director,it.casts, it.country, it.userDating)
                        movieList.add(movie)
                        homeAdapter.submitList(movieList)
                    }
                } else {
                    Log.d("HomeFragment Firestore", "Data is not found")
                }
            }
        }

        homeAdapter = HomeAdapter(onItemClicked = {
            val intent = Intent(context, ReviewActivity::class.java)
            intent.putExtra("movieModel", it)
            startActivity(intent)
        })

        fragmentHomeBinding.recyclerViewMovies.layoutManager = GridLayoutManager(context, 3)
        fragmentHomeBinding.recyclerViewMovies.adapter = homeAdapter

        //createMovieList()
    }


    override fun onResume() {
        super.onResume()
        homeAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        movieList = mutableListOf()
    }

    private fun createMovieList() {
        var movie = Movie("0", "헤어질 결심", "https://w.namu.la/s/a1e10af8e9dce4bddecdb688e9bdf682a6a18b48b0672f62e5b0a84a2a47975a11d2557feb69fb96720b262ae47fdb92d26b9ec366587d1d8887c23118198bb45b3f0c3775f06884fb2e5fb3abb20c9deecc7850b21ee28e88e86754c0ac5787a2dae863c001c6ca56ddf636297c68db", "2021",
            "박찬욱", "박해일, 탕웨이, 이정현","대한민국", "8.68")
        movieColl.add(movie)
        movie = Movie("1", "어벤져스: 인피니티 워", "https://w.namu.la/s/c6c3725495db95fc53c169a97aa299ab2923459f103b3ebb563fcb3da1188885d76762c26cd417f04d235728e16613515812f499400ab9565a9199ecc2b9627d5b6b083916ac431aa931594164943f233b4cf43e32499019efc7fcdb6e06d00e40a15144d8711a8473f3f4b5a52c4173", "2018",
            "루소 형제","로버트 다우니 주니어, 크리스 햄스워스, 베네딕트 컴버배치", "미국", "9.08")
        movieColl.add(movie)
        movie = Movie("2", "마션", "https://w.namu.la/s/caae5cba0fa2df86eb15be6684a7c8245cb1e71782b3a9ec23c3c42ba6dc90c7f757bcef2cf13b8be7c018221e420e3ba2d0c0b4d684fb6fcdaf9bf3f92919913735f65567e7956983d3381a73126e7196522bcb961e40b6dcc2668c7fe9f25428f34788ac6288af37f4c110e3fb215b", "2015",
            "리들리 스콧", "맷 데이먼, 제시카 차스테인", "미국,영국", "8.72")
        movieColl.add(movie)
        movie = Movie("3", "아바타: 물의 길", "https://w.namu.la/s/ee34068e9f45d450104331141e2c1609b88c58022fb91fd78cc81e369fc163acecf08c12f33bb848cfe15b7713be37368b5b1edd591111304b7443062f2f466993414f4e24d2707f4307392c5b57aac2938db62d3dba29c7d67dfedd5714644a7abef6ba37ab09e0a6cd5b5298db9f5a", "2022",
            "제임스 카메론", "조 샐다나, 샘 워싱턴, 시고니 위버","미국", "8.95")
        movieColl.add(movie)
        movie = Movie("4", "범죄도시2", "https://w.namu.la/s/2b96c1f6b73672e8bbed26865bda4680983f887ffb4ea3a571dfe49719c547a1a2c9d1807ab610e9b55ffdd0b6826f24121d6e4fc3784f2c477e02395034d082b72a23205e1476dff51ebd2d33b53a116b5251a3dea984e27c0f92d6837bede440734a8e5d36ba11164c495f03d1eb67", "2022",
            "이상용","마동석, 손석구", "대한민국", "9.01")
        movieColl.add(movie)
        movie = Movie("5", "업", "https://w.namu.la/s/28358fbf4faa6dc5bd7f054a067844852cf735e3755b116aaecccdb5db97560eecd02abbba38c2c8741608a2303ca93b9264ef82aa45a01d04d57dfce56614604f08426e7ec792f39c1d489e2e8e7054d2bd5db54b2f5017791512448fe6d46453b693b6d84d10d963b50658429442a8", "2009",
            "피트 닥터, 밥 피터슨", "에드워드 애스너, 조던 나가이","미국", "9.33")
        movieColl.add(movie)
        movie = Movie("6", "해리 포터와 마법사의 돌", "https://w.namu.la/s/202aeccc915b7f3d58b9454f5b69bdc474ccfa0b2d3520639973f84b07f880ad176fcb03c9e765ea80d5bda5cc097560e81006af5632903224bc7d0de3037eb04a8c34005cb8effab57eaa91447e716bd727f1a50d2a4ff3003431f7b086d7969f16526a0936780a49916e3d93c9ddd3", "2001",
            "크리스 콜럼버스","다니엘 래드클리프, 루퍼트 그린트, 엠마 왓슨", "영국,미국", "9.39")
        movieColl.add(movie)
        movie = Movie("7", "어바웃 타임", "https://w.namu.la/s/45540343663db139cc66f875ddf9a2b68c15b17cd5957e2cc6f436d37c3918d1902911c08209621b835480c23ae8bedee3e2a3681d397c1c7a4d8b3cf6acd803c3d0d57478d1ac0499aeca7c686ee34ca301665ea1069383e4fa1db94d42c4641363c2e1a88ceb171c457de6547a8e10", "2013",
            "리차드 커티스","도널 글리슨, 레이첼 맥아담스", "영국", "9.30")
        movieColl.add(movie)
        movie = Movie("8", "셜록홈즈: 그림자 게임", "https://w.namu.la/s/802a128cb32951cfb344eea03cf51a502046dab5df1180fe62efc490c82469963d7fcff20e6e8f87598c50c5dfc76a95f3886b5e99f2959e56b6619f6719877bc80f93d67a45a33d996827489879609da9060fb305d33b6fa089ef7c071dcc7ba2a2a5c030c4bc60ed7fc9270b9f0cb8", "2011",
            "가이 리치", "로버트 다우니 주니어, 주드 로","미국", "8.00")
        movieColl.add(movie)
        movie = Movie("9", "탑건: 매버릭", "https://w.namu.la/s/4435962b2e93f4e284bb36a412158061bba6363c41fab8033c7ec0198d2caa75fe4d0a84460ddfc49d9b90ecc30f5e218d0e04ba3dfdac3cdc18e2902d60ccc1bdc46ee5f653bed3ec039c21a0073148ec78502155ba335598be8a7a529e8e60", "2022",
            "조셉 코신스키","톰 크루즈, 마일즈 텔러, 제니퍼 코넬리", "미국", "9.60")
        movieColl.add(movie)
        movie = Movie("10", "하울의 움직이는 성", "https://w.namu.la/s/9be7229f416237598a607ab7488cdeefa4c875bed5c91244b81bf2b42794bc25dff5d7e91f9a070448221268c67589e6324d73b06d97141878d59da034c59e27666f8ded79338e0927c8a98d190267bdd82a14704f6d62ecd7d966c9833c1ff21cef4c51264c624dddad6e0cf7bd29bc", "2004",
            "미야자키 하야오","바이쇼 치에코, 기무라 타쿠야", "일본", "9.35")
        movieColl.add(movie)
        movie = Movie("11", "기생충", "https://w.namu.la/s/be4b5f93d882f07eed700cfbbff75cf0cf8838041770891f0da80afe53c700b39579a3e7357c0accf4455a7afa4fd7e8860ddb2db229f0df2375078cdea5a70243cdc3200ee05195b7ae3b062fe6683a6c90fa01bd6ffb6b0c011902c9dd1c52d739f758a38c375a1f9f484f7032baa3", "2019",
            "봉준호","송강호, 이선균, 조여정, 최우식, 박소담", "대한민국", "9.07")
        movieColl.add(movie)
    }
}