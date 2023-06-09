package com.example.museumticketshop.repositories;

import com.example.museumticketshop.entities.Exhibition;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class ExhibitionDao {
    private static ExhibitionDao instance;
    private static final String COLLECTION_NAME = "exhibitions";
    private ExhibitionDao() {
    }
    public static ExhibitionDao getInstance() {
        if (instance == null) {
            instance = new ExhibitionDao();
        }
        return instance;
    }

    @Deprecated
    public Task<Void> addExhibition(Exhibition exhibition) {
        // implemented for startup & testing purposes only
        FirebaseFirestore firestoreReference = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestoreReference.collection(COLLECTION_NAME).document();
        exhibition.setId(docRef.getId());
        return docRef.set(exhibition);
    }

    public Task<Exhibition> getExhibitionById(String id) {
        FirebaseFirestore firestoreReference = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestoreReference.collection(COLLECTION_NAME).document(id);
        return docRef.get().continueWith(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                return snapshot.toObject(Exhibition.class);
            } else {
                throw new DaoException(
                        String.format("Exhibition with id %s could not be loaded!", id));
            }
        });
    }

    public Task<List<Exhibition>> getAllExhibitions() {
        FirebaseFirestore firestoreReference = FirebaseFirestore.getInstance();
        CollectionReference colRef = firestoreReference.collection(COLLECTION_NAME);
        // we do not display the exhibitions on the main page which have a description
        // that is too long
        // we also display 5 exhibitions max, ordered by which have been opened most recently
        // the user can still buy tickets for unlisted exhibitions, they will be listed in
        // the ticket buy view
        Query query = colRef
                .whereLessThanOrEqualTo("descriptionLength", 200)
                .orderBy("descriptionLength") // needs to be ordered by length first ...
                .orderBy("openFrom", Query.Direction.DESCENDING)
                .limit(5);
        return query.get().continueWith(task -> {
            QuerySnapshot snapshot = task.getResult();
            return snapshot.getDocuments()
                    .stream().map(doc -> doc.toObject(Exhibition.class))
                    .collect(Collectors.toList());
        });
    }
}
