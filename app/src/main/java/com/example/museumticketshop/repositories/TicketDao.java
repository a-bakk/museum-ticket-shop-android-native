package com.example.museumticketshop.repositories;

import com.example.museumticketshop.entities.Ticket;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class TicketDao {
    private static TicketDao instance;
    private static final String COLLECTION_NAME = "tickets";
    private TicketDao() {

    }
    public static TicketDao getInstance() {
        if (instance == null) {
            instance = new TicketDao();
        }
        return instance;
    }

    public Task<Void> createTicket(Ticket ticket) {
        FirebaseFirestore firestoreRef = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestoreRef.collection(COLLECTION_NAME).document();
        ticket.setId(docRef.getId());
        return docRef.set(ticket);
    }

    public Task<Ticket> readTicketById(String id) {
        FirebaseFirestore firestoreRef = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = firestoreRef.collection(COLLECTION_NAME);
        DocumentReference documentRef = firestoreRef.collection(COLLECTION_NAME).document(id);
        return documentRef.get().continueWith(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                return snapshot.toObject(Ticket.class);
            } else {
                throw new DaoException(
                        String.format("Ticket with id %s could not be loaded!", id));
            }
        });
    }

    public Task<List<Ticket>> readTicketsByEmail(String email) {
        FirebaseFirestore fireStoreRef = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = fireStoreRef.collection(COLLECTION_NAME);
        Query query = collectionRef.whereEqualTo("userEmail", email)
                .orderBy("date", Query.Direction.ASCENDING);
        return query.get().continueWith(task -> task.getResult().getDocuments()
                .stream().map(doc -> doc.toObject(Ticket.class))
                .collect(Collectors.toList()));
    }

    public Task<Void> updateTicket(String id, Ticket ticket) {
        FirebaseFirestore firestoreRef = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestoreRef.collection(COLLECTION_NAME).document(id);
        return docRef.set(ticket);
    }

    public Task<Void> deleteTicket(String id) {
        FirebaseFirestore firestoreRef = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestoreRef.collection(COLLECTION_NAME).document(id);
        return docRef.delete();
    }
}
