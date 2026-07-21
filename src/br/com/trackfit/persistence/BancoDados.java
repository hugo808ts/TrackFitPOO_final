package br.com.trackfit.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BancoDados {
    private final File arquivo;

    public BancoDados(String caminhoArquivo) {
        this.arquivo = new File(caminhoArquivo);
    }

    public DadosSistema carregar() {
        if (!arquivo.exists()) {
            return new DadosSistema();
        }

        ObjectInputStream entrada = null;
        try {
            entrada = new ObjectInputStream(new FileInputStream(arquivo));
            return (DadosSistema) entrada.readObject();
        } catch (IOException e) {
            throw new PersistenciaException("Nao foi possivel ler o banco de dados.", e);
        } catch (ClassNotFoundException e) {
            throw new PersistenciaException("Formato do banco de dados invalido.", e);
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException e) {
                    throw new PersistenciaException("Nao foi possivel fechar o arquivo de leitura.", e);
                }
            }
        }
    }

    public void salvar(DadosSistema dados) {
        File pasta = arquivo.getParentFile();
        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }

        ObjectOutputStream saida = null;
        try {
            saida = new ObjectOutputStream(new FileOutputStream(arquivo));
            saida.writeObject(dados);
        } catch (IOException e) {
            throw new PersistenciaException("Nao foi possivel salvar o banco de dados.", e);
        } finally {
            if (saida != null) {
                try {
                    saida.close();
                } catch (IOException e) {
                    throw new PersistenciaException("Nao foi possivel fechar o arquivo de escrita.", e);
                }
            }
        }
    }
}
