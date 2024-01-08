
package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Cliente;
import tools.FactoryPostgres;

public class ClienteDAO {
    private Connection c;

    public ClienteDAO() {
        this.c = FactoryPostgres.getConexaoPostgres();
    }
    
    
    public boolean insert(Cliente cliente) {
        String sql = "INSERT INTO arthur_ribeiro.Clientes(nome, online) VALUES (?,?) returning id";
        
        try(PreparedStatement trans = this.c.prepareStatement(sql)){
            trans.setString(1, cliente.getNome());
            trans.setBoolean(2, cliente.isOnline());
            
            ResultSet resultado = trans.executeQuery();
            if(resultado.next()) {
                cliente.setId(resultado.getInt("id"));
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return true;
    }
    
    public ArrayList<Cliente> selectAll() {
        ArrayList<Cliente> retorno = new ArrayList<>();
        
        String sql = "SELECT nome, id, online FROM arthur_ribeiro.Clientes ORDER BY online DESC";
        
        try(PreparedStatement trans = this.c.prepareStatement(sql)) {
            ResultSet resultado = trans.executeQuery();
            
            while (resultado.next()) {                
                Cliente cliente = new Cliente();
                cliente.setOnline(resultado.getBoolean("online"));
                cliente.setId(resultado.getInt("id"));
                cliente.setNome(resultado.getString("nome"));
            
                retorno.add(cliente);
            }
            
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return retorno;
    }
    
    public Cliente selectByName(String name) {
        Cliente retorno;
        String sql = "SELECT nome, id, online FROM arthur_ribeiro.Clientes WHERE nome = ?";
        try(PreparedStatement trans = this.c.prepareStatement(sql)) {
            trans.setString(1, name);
            
            ResultSet resultado = trans.executeQuery();
            if(resultado.next()) {
                retorno = new Cliente();
                retorno.setOnline(resultado.getBoolean("online"));
                retorno.setId(resultado.getInt("id"));
                retorno.setNome(name);
                
                return retorno;
            }else {
                return null;
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    public Cliente selectById(int id) {
        Cliente retorno;
        String sql = "SELECT nome, id, online FROM arthur_ribeiro.Clientes WHERE id = ?";
        try(PreparedStatement trans = this.c.prepareStatement(sql)) {
            trans.setInt(1, id);
            
            ResultSet resultado = trans.executeQuery();
            if(resultado.next()) {
                retorno = new Cliente();
                retorno.setOnline(resultado.getBoolean("online"));
                retorno.setId(id);
                retorno.setNome(resultado.getString("nome"));
                
                return retorno;
            }else {
                return null;
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    public boolean setOnline(boolean online, long id) {
        String sql = "UPDATE arthur_ribeiro.Clientes SET online = ? WHERE id = ?";
        
        try(PreparedStatement trans = this.c.prepareStatement(sql)) {
            trans.setBoolean(1, online);
            trans.setLong(2, id);
            
            trans.execute();
            return true;
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
