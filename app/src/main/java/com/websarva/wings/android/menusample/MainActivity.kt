package com.websarva.wings.android.menusample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    // リストビューに表示するデータ。
    private var _menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
    // SimpleAdapter 第4引数fromデータ用の用意
    val _from = arrayOf("name", "price")
    // SimpleAdapter 第5引数toデータ用の用意
    val _to = intArrayOf(R.id.tvMenuNameRow, R.id.tvMenuPriceRow)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 定食メニューListオブジェクトをprivateメソッドを利用して用意し、プロパティに格納
        _menuList = createTeishokuList()
        // 画面部品ListViewを取得
        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        // SimpleAdapterを生成
        val adapter = SimpleAdapter(this@MainActivity, _menuList, R.layout.row, _from, _to)

        // アダプタを登録
        lvMenu.adapter = adapter
        // リストタップのリスナクラス登録
        lvMenu.onItemClickListener = ListItemClickListener()

        registerForContextMenu(lvMenu)
    }

    private fun createTeishokuList(): MutableList<MutableMap<String, Any>> {
        // 定食メニュー用のListオブジェクトを用意
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
        // から揚げ定食 のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        var menu = mutableMapOf<String, Any>("name" to "から揚げ定食", "price" to 800, "desc" to "若鶏のから揚げにサラダ、ご飯とお味噌汁が付きます。")
        menuList.add(menu)
        // ハンバーグ定食 のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        menu = mutableMapOf<String, Any>("name" to "ハンバーグ定食", "price" to 850, "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。")
        menuList.add(menu)
        return menuList
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // タップされた行のデータを取得 SimpleAdapterでは1行分のデータは MutableMap型！
            val item = parent.getItemAtPosition(position) as MutableMap<String,Any>

            order(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // オプションメニュー用xmlファイルをインフレイト
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)
        return true
    }

    override fun onCreateContextMenu(menu: ContextMenu, view: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo)
        // コンテキストメニュー用xmlファイルをインフレイト
        menuInflater.inflate(R.menu.menu_context_menu_list, menu)
        // コンテキストメニューのヘッダタイトルを設定
        menu.setHeaderTitle(R.string.menu_list_context_header)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        // 長押しされたビューに関する情報が格納されたオブジェクトを取得
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        // 長押しされたリストのポジションを取得
        val listPosition = info.position
        // ポジションから長押しされたメニュー情報Mapオブジェクトを取得
        val menu = _menuList[listPosition]

        when(item.itemId){
            R.id.menuListContextDesc -> {
                val desc = menu["desc"] as String
                Toast.makeText(this@MainActivity, desc, Toast.LENGTH_LONG).show()
            }
            R.id.menuListContextOrder ->
                order(menu)
            else ->
                returnVal = super.onContextItemSelected(item)

        }

        return returnVal
    }

    private fun order(menu: MutableMap<String, Any>) {
        // 定食名と金額を取得
        val menuName = menu["name"] as String
        val menuPrice = menu["price"] as Int

        // インテントオブジェクトを生成
        val intent2MenuThanks = Intent(this@MainActivity, MenuThanksActivity::class.java)

        // 第2画面に送るデータを格納
        intent2MenuThanks.putExtra("menuName", menuName)
        intent2MenuThanks.putExtra("menuPrice", "${menuPrice}円")

        // 第2画面の起動
        startActivity(intent2MenuThanks)
    }
}