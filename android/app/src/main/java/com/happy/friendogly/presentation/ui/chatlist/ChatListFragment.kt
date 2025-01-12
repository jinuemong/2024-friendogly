package com.happy.friendogly.presentation.ui.chatlist

import androidx.fragment.app.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.FragmentChatListBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.ui.chatlist.adapter.ChatListAdapter
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatActivity

class ChatListFragment :
    BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list),
    ChatListNavigationAction {
    private val viewModel: ChatListViewModel by viewModels {
        ChatListViewModel.factory(
            AppModule.getInstance().getChatListUseCase,
        )
    }
    private lateinit var adapter: ChatListAdapter

    override fun initViewCreated() {
        initAdapter()
        binding.vm = viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.getChats()
    }

    private fun initAdapter() {
        adapter = ChatListAdapter(this)
        binding.rcvChatList.adapter = adapter
        viewModel.getChats()
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            adapter.submitList(chats)
        }
        swipeEvent()
    }

    private fun swipeEvent() {
        binding.swipelayoutChatListRefresh.setOnRefreshListener {
            viewModel.getChats()
            binding.swipelayoutChatListRefresh.isRefreshing = false
        }
    }

    override fun navigateToChat(chatId: Long) {
        startActivity(ChatActivity.getIntent(requireContext(), chatId))
    }

    companion object {
        const val TAG = "ChatListFragment"
    }
}
