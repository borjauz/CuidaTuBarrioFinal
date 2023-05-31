package com.example.cuidatubarriofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuidatubarriofinal.dto.ComentarioDTO;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private List<ComentarioDTO> comentarios;
    private LayoutInflater inflater;
    private Context context;

    public ListViewAdapter(Context context, List<ComentarioDTO> comentarios) {
        this.comentarios = comentarios;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return comentarios.size();
    }

    @Override
    public Object getItem(int position) {
        return comentarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.usuarioTextView = convertView.findViewById(R.id.usuarioTextView);
            viewHolder.comentarioTextView = convertView.findViewById(R.id.comentarioTextView);
            viewHolder.deleteButton = convertView.findViewById(R.id.deleteButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ComentarioDTO comentario = comentarios.get(position);
        viewHolder.usuarioTextView.setText(comentario.getUsuario());
        viewHolder.comentarioTextView.setText(comentario.getComentario());


        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para borrar el comentario
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView usuarioTextView;
        TextView comentarioTextView;
        Button deleteButton;
    }
}