package com.joinhub.complaintprotaluser.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.Adapters.ChatRecyclerAdapter
import com.joinhub.complaintprotaluser.databinding.FragmentLiveChatBinding
import com.joinhub.complaintprotaluser.models.Chat
import com.joinhub.complaintprotaluser.models.ChatList
import com.joinhub.complaintprotaluser.utilties.Constants
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.getDate

class LiveChatFragment: Fragment() {
    private lateinit var key: String
    private lateinit var mChatRecyclerAdapter: ChatRecyclerAdapter
    private  lateinit var binding : FragmentLiveChatBinding
    private  lateinit var chatList:MutableList<Chat>
    private  lateinit var chatLists:ChatList
    lateinit var preference: Preference
    lateinit var rooms:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentLiveChatBinding.inflate(layoutInflater,container,false)
        chatList = mutableListOf()
        chatLists= ChatList()
        init()

        preference= Preference(requireContext())
        binding.imageSend.setOnClickListener {
            val chat = Chat(
                preference.getStringpreference("userEmail",null),
                preference.getStringpreference("serviceEmail",null),
                preference.getStringpreference("userName",null),
                preference.getStringpreference("serviceUserName",null),
                binding.edittxtType.text.toString().trim(),
                System.currentTimeMillis()
            )
            sendMessage(chat)
        }
        getMessageFromFirebaseUser(preference.getStringpreference("userName",null),
        preference.getStringpreference("serviceUserName"))
        return binding.root
    }

    private fun sendMessage(chat: Chat) {
        val room_type_1 = chat.senderUid + "_" + chat.receiverUid
        val room_type_2 = chat.receiverUid + "_" + chat.senderUid

        val databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child(Constants.ARG_CHAT_ROOMS).ref.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                when {
                    dataSnapshot.hasChild(room_type_1) -> {

                        databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_1)
                            .child((chat.timestamp).toString()).setValue(chat)
                        rooms= room_type_1
                    }
                    dataSnapshot.hasChild(room_type_2) -> {
                        databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_2)
                            .child((chat.timestamp).toString()).setValue(chat)
                        rooms= room_type_2
                    }
                    else -> {
                        databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_1)
                            .child((chat.timestamp).toString()).setValue(chat)
                        rooms= room_type_1
                        getMessageFromFirebaseUser(chat.senderUid, chat.receiverUid)

                    }
                }
                // send push notification to the receiver
//                sendPushNotificationToReceiver(
//                    chat.sender,
//                    chat.message,
//                    chat.senderUid,
//                    SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN),
//                    receiverFirebaseToken
//                )
                binding.edittxtType.setText("")
                Toast.makeText(activity, "Message sent", Toast.LENGTH_SHORT)
                    .show()
                setChatList(chat)
            }

            override fun onCancelled(databaseError: DatabaseError) {
               /// mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.message)
            }
        })
    }

    private fun setChatList(chat: Chat) {
        val ref= FirebaseDatabase.getInstance().reference.child("ChatList").child(preference.getStringpreference("serviceUserName",null))

        ref.orderByChild("userName").equalTo(preference.getStringpreference("userName",null))
            .addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    chatLists= snap.getValue(ChatList::class.java)!!
                    key= snap.key!!
                }
                if(chatLists.name==""){
                    ref.push().setValue(
                        ChatList(rooms,preference.getStringpreference("userFullName",null),
                        preference.getStringpreference("userName",null),chat.message,chat.timestamp,
                        chat.senderUid,false)
                    )
                }else{

                    ref.child(key).child("lastMessage").setValue(chat.message)
                    ref.child(key).child("timestamp").setValue(chat.timestamp)
                    ref.child(key).child("isRead").setValue(false)
                    ref.child(key).child("senderId").setValue(chat.senderUid)


                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getMessageFromFirebaseUser(senderUid: String, receiverUid: String) {
        val room_type_1: String = senderUid + "_" + receiverUid
        val room_type_2: String = receiverUid + "_" + senderUid

        val databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child(Constants.ARG_CHAT_ROOMS).ref.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                when {
                    dataSnapshot.hasChild(room_type_1) -> {

                        FirebaseDatabase.getInstance()
                            .reference
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_1).addChildEventListener(object : ChildEventListener {
                                override fun onChildAdded(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                    val chat = dataSnapshot.getValue(Chat::class.java)
                                    updateRec(chat)
                                }

                                override fun onChildChanged(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                }

                                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                                override fun onChildMoved(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                              //      mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.message)
                                }
                            })
                    }
                    dataSnapshot.hasChild(room_type_2) -> {
                        FirebaseDatabase.getInstance()
                            .reference
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_2).addChildEventListener(object : ChildEventListener {
                                override fun onChildAdded(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                    val chat = dataSnapshot.getValue(Chat::class.java)
                     //               mOnGetMessagesListener.onGetMessagesSuccess(chat)
                                updateRec(chat)
                                }

                                override fun onChildChanged(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                }

                                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                                override fun onChildMoved(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                       //             mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.message)
                                }
                            })
                    }
                    else -> {

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            //    mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.message)
            }
        })
    }

    private fun updateRec(chat: Chat?) {

        mChatRecyclerAdapter.add(chat)
        binding.chatRec.smoothScrollToPosition(mChatRecyclerAdapter.itemCount - 1)
    }

    private fun init() {
            mChatRecyclerAdapter = ChatRecyclerAdapter(ArrayList<Chat>(), requireContext())
            binding.chatRec.adapter = mChatRecyclerAdapter


    }
}