package br.com.secompufscar.secomp_ufscar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.secompufscar.secomp_ufscar.data.Atividade;
import br.com.secompufscar.secomp_ufscar.listaAtividades.ListaQuarta;
import br.com.secompufscar.secomp_ufscar.listaAtividades.ListaQuinta;
import br.com.secompufscar.secomp_ufscar.listaAtividades.ListaSegunda;
import br.com.secompufscar.secomp_ufscar.listaAtividades.ListaSexta;
import br.com.secompufscar.secomp_ufscar.listaAtividades.ListaTerca;

public class Cronograma extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private View loadingView;
    private GetAtividades getAtividades;

    private int current_tab;

    public Cronograma() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cronograma, container, false);

        loadingView = view.findViewById(R.id.loading_spinner_cronograma);
        loadingView.setVisibility(View.GONE);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                current_tab = tab.getPosition();
                Log.d("teste on select", String.valueOf(MainActivity.current_tab));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onResume(){
        getAtividades = new GetAtividades();
        getAtividades.execute();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.current_tab = current_tab;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        ListaSegunda listaSeg = new ListaSegunda();
        Bundle paramsSeg = new Bundle();
        paramsSeg.putInt("offset", 0);
        listaSeg.setArguments(paramsSeg);
        adapter.addFragment(listaSeg, "Seg");

        ListaTerca listaTer = new ListaTerca();
        Bundle paramsTer = new Bundle();
        paramsTer.putInt("offset", 1);
        listaTer.setArguments(paramsTer);
        adapter.addFragment(listaTer, "Ter");

        ListaQuarta listaQua = new ListaQuarta();
        Bundle paramsQua = new Bundle();
        paramsQua.putInt("offset", 2);
        listaQua.setArguments(paramsQua);
        adapter.addFragment(listaQua, "Qua");

        ListaQuinta listaQui = new ListaQuinta();
        Bundle paramsQui = new Bundle();
        paramsQui.putInt("offset", 3);
        listaQui.setArguments(paramsQui);
        adapter.addFragment(listaQui, "Qui");

        ListaSexta listaSex = new ListaSexta();
        Bundle paramsSex = new Bundle();
        paramsSex.putInt("offset", 4);
        listaSex.setArguments(paramsSex);
        adapter.addFragment(listaSex, "Sex");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    private class GetAtividades extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingView.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (MainActivity.get_atividades_from_server) {
                Log.d("teste", "pegando do server");
                Atividade.getAtividadesFromHTTP(getActivity());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            MainActivity.get_atividades_from_server = false;

            setupViewPager(viewPager);

            try {
                Log.d("teste on try", String.valueOf(MainActivity.current_tab));

                tabLayout.getTabAt(MainActivity.current_tab).select();
            } catch (Exception e) {
                e.printStackTrace();
                tabLayout.getTabAt(0).select();
            }

            loadingView.setVisibility(View.GONE);
            viewPager.setAlpha(0f);

            viewPager.setVisibility(View.VISIBLE);

            viewPager.animate()
                    .alpha(1f)
                    .setDuration(getResources().getInteger(
                            android.R.integer.config_longAnimTime))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loadingView.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
