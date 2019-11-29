/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestiondesvols;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionDesVols {
    
    
    public Connection conn = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        GestionDesVols gestion;
        
        gestion = new GestionDesVols();
        
        //gestion.printAllAvion();
        
        //gestion.printPilote();
        
        //gestion.printAverageWage();
        
        //gestion.printSumPlaneCapacity();
        
        //gestion.majLocalisation();
        
        //gestion.printAllAvion();
        
        //gestion.insertAvion(11,"A380",380);
        //gestion.insertAvion(66,"A66",666);
        
        //gestion.printAllAvion();
        
        gestion.searchVol("0001-01-01", "Nice");
        
        try {
            gestion.conn.close();
        } catch (SQLException e) {
            System.err.println("Erreur : " + e.getMessage());        
        }
        
    }
    
    public GestionDesVols(){
        
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver trouvé.");
            
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String pwd = "postgres";
            conn = DriverManager.getConnection(url,user,pwd);
            
            if (conn != null){
                System.out.println("Connexion établie.");
            } else {
                System.out.println("Connexion introuvable.");
            }
            
            
            
        }catch ( ClassNotFoundException | SQLException e ) {
            System.err.println("Erreur : " + e.getMessage());
        } 
    }
    
    public void printAllAvion () {
        
        String sql = "SELECT * FROM avion";
        
        try ( Statement stmt = conn.createStatement(); ) {

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println();
            while (rs.next()){
                System.out.println("\t Numéro : "+ rs.getInt("avnum")+"\t");          
                System.out.println("\t Nom : "+ rs.getString("avnom")+"\t");          
                System.out.println("\t Capacité : "+ rs.getInt("capacite")+"\t");          
                System.out.println("\t Localisation : "+ rs.getString("localisation")+"\t");          
                System.out.println("\n---------------------------------");          
            }
            System.out.println();
            
            rs.close();
            
        } catch (SQLException e){
            System.err.println("Erreur : " + e.getMessage());
        }
        
    }
    
    public void printPilote () {
        String sql = "SELECT * FROM pilote";
        
        try ( Statement stmt = conn.createStatement(); ) {

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println();
            while (rs.next()){
                System.out.println("\t Numéro : "+ rs.getInt("plnum")+"\t");          
                System.out.println("\t Nom : "+ rs.getString("plnom")+"\t");
                System.out.println("\t Prenom : "+ rs.getString("plprenom")+"\t");
                System.out.println("\t Ville : "+ rs.getString("ville")+"\t");          
                System.out.println("\t Date de naissance : "+ rs.getDate("datenaiss")+"\t");          
                System.out.println("\t Salaire : "+ rs.getFloat("salaire")+"\t");
                System.out.println("\n---------------------------------");          
            }
            System.out.println();
            
            rs.close();
            
        } catch (SQLException e){
            System.err.println("Erreur : " + e.getMessage());
        }
    }
    
    public void printAverageWage() {
        
        String sql = "SELECT avg(salaire) as salairemoyen  FROM pilote";
        
        try ( Statement stmt = conn.createStatement(); ) {

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println();
            while (rs.next()){
                System.out.println("\t Salaire moyen des pilotes : "+ rs.getFloat("salairemoyen")+"\t");          
                System.out.println("\n---------------------------------");          
            }
            System.out.println();
            
            rs.close();
            
        } catch (SQLException e){
            System.err.println("Erreur : " + e.getMessage());
        }
    }
    
    public void printSumPlaneCapacity() {
        
        String sql = "SELECT sum(capacite) as total FROM avion";
        
        try ( Statement stmt = conn.createStatement(); ) {

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println();
            while (rs.next()){
                System.out.println("\t Capacite totale des avions : "+ rs.getInt("total")+"\t");          
                System.out.println("\n---------------------------------");          
            }
            System.out.println();
            
            rs.close();
            
        } catch (SQLException e){
            System.err.println("Erreur : " + e.getMessage());
        }
    }
    
    public void majLocalisation() {
        
        String sql = "UPDATE avion SET localisation = 'Toulouse' WHERE avnom = 'A300'";
        
        try ( Statement stmt = conn.createStatement(); ) {

            int result = stmt.executeUpdate(sql);
            System.out.println();
            System.out.printf("Nombre d'avions modifiés : %d\n",result);
                        
        } catch (SQLException e){
            System.err.println("Erreur : " + e.getMessage());
        }
        
    }
    
    public void insertAvion(int num, String nom, int capacite) {
        
        String sql = "INSERT INTO avion (avnum, avnom, capacite) VALUES (?,?,?)";
        
        try ( PreparedStatement stmt = conn.prepareStatement(sql); ) {

            stmt.setInt(1,num);
            stmt.setString(2, nom);
            stmt.setInt(3, capacite);
            
            int result = stmt.executeUpdate();
            
            if (result == 0) {
                throw new SQLException ("Erreur dans l'insertion");
            }
            System.out.println();
            System.out.printf("Nombre d'avions insérés : %d\n",result);
                        
        } catch (SQLException e){
            System.err.println("Erreur : " + e.getMessage());
        }
        
    }
    
    public void searchVol(String heureDep, String villeDep) {
        
        String sql = "SELECT * FROM vol WHERE heureDep = ? and villeDep = ?";        
        try ( PreparedStatement stmt = conn.prepareStatement(sql); ) {

            stmt.setString(1, heureDep);
            stmt.setString(2, villeDep);
    
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()){
                System.out.println("\n VOL TROUVE");
                System.out.println("\t Numéro : "+ rs.getInt("volnum")+"\t");          
                System.out.println("\t Pilote : "+ rs.getInt("plnum")+"\t");
                System.out.println("\t Avion : "+ rs.getInt("avnum")+"\t");
                System.out.println("\t Ville de départ : "+ rs.getString("villedep")+"\t");          
                System.out.println("\t Ville d'arrivé : "+ rs.getString("villearr")+"\t");          
                System.out.println("\t Heure de départ : "+ rs.getDate("heuredep")+"\t");
                System.out.println("\t Heure d'arrivé : "+ rs.getDate("heurearr")+"\t");  
                System.out.println("\n---------------------------------");
            }
            
            System.out.println();
            
                        
        } catch (SQLException e){
            System.err.println("Erreur : " + e.getMessage());
        }
        
    }
    
}
