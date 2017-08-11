package br.com.secompufscar.secomp_ufscar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.secompufscar.secomp_ufscar.data.Atividade;
import br.com.secompufscar.secomp_ufscar.data.DatabaseHandler;
import br.com.secompufscar.secomp_ufscar.utilities.ClickListener;
import br.com.secompufscar.secomp_ufscar.utilities.RecyclerTouchListener;


public class MinhasAtividades extends Fragment {
    
    public static List<Atividade> atividadeList = new ArrayList<>();
    private RecyclerView recycler_atividades;
    private AtividadesAdapter adapter;

    public MinhasAtividades() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AtividadesAdapter(getActivity(), atividadeList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_atividades, container, false);
        recycler_atividades = (RecyclerView) view.findViewById(R.id.recycler_atividades);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recycler_atividades.setLayoutManager(mLayoutManager);

        recycler_atividades.setAdapter(adapter);

        recycler_atividades.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recycler_atividades, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Atividade atividade = atividadeList.get(position);

                Context context = view.getContext();
                Intent detalhesAtividade = new Intent(context, AtividadeDetalhes.class);
                detalhesAtividade.putExtra("id_atividade", atividade.getId());
                context.startActivity(detalhesAtividade);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        new UpdateAtividades().execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new UpdateAtividades().execute();
    }



    private class UpdateAtividades extends AsyncTask<Void, Void, List<Atividade>> {
        @Override
        protected List<Atividade> doInBackground(Void... params) {
            try {
                return DatabaseHandler.getDB().getAllFavoritos();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Atividade> atividadesFromDB) {
            atividadeList.clear();
            atividadeList.addAll(atividadesFromDB);
            adapter.notifyDataSetChanged();
        }
    }
}