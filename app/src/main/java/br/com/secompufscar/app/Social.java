package br.com.secompufscar.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class Social extends AppCompatActivity {
    FragmentManager fm;
    TwitterHashtag twitterHashtag;
    Facebook facebook;
    Instagram instagram;
    FragmentTransaction ft;
    private TextView textView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.twitter:
                    ft.replace(R.id.content,twitterHashtag, "twitter");
//                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                case R.id.instagram:
                    ft.replace(R.id.content,instagram, "instagram");
//                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                case R.id.facebook:
                    ft.replace(R.id.content, facebook, "facebook");
//                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                /*
                    COMENTEI A PARTE DE ADICIONAR PARA A STACK PORQUE NÃO ACHO QUE SEJA INTERESSANTE
                    SE TEM AS TABS ALI EMBAIXO, NÃO TEM PRA QUE VOLTAR PRA UMA OUTRA TAB, E SIM SÓ
                    SAIR DA ATIVIDADE
                 */

            }

            return false;
        }

    };
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redes_sociais);
        textView = (TextView)findViewById(R.id.fragment_title);
        //INICIO DA GAMBIARRA
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        textView.setText("Redes Sociais");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //FIM DA GAMBIARRA
        twitterHashtag = new TwitterHashtag();
        facebook = new Facebook();
        instagram = new Instagram();
        fm =  getSupportFragmentManager();
        ft = fm.beginTransaction();
        if(ft.isEmpty()){
            ft.add(R.id.content, twitterHashtag);
            ft.commit();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
}
